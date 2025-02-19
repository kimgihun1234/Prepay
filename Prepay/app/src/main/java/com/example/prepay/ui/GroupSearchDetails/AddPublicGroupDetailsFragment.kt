package com.example.prepay.ui.GroupSearchDetails

import android.animation.ValueAnimator
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri 
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentPublicGroupDetailsBinding
import com.example.prepay.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.prepay.CommonUtils
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamDetailsRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.ui.GroupSearch.GroupSearchFragmentViewModel
import com.example.prepay.ui.GroupSearch.PublicSearchAdapter
import com.example.prepay.ui.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
//import com.google.zxing.BarcodeFormat
//import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

private const val TAG = "PublicGroupDetailsFragment_싸피"

class AddPublicGroupDetailsFragment : BaseFragment<FragmentPublicGroupDetailsBinding>(
    FragmentPublicGroupDetailsBinding::bind,
    R.layout.fragment_public_group_details
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private val viewModel: GroupSearchDetailsViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var teamsRes: PublicTeamDetailsRes
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageMultipart: MultipartBody.Part? = null
    private var heartCheck =false
    var lat = 36.5
    var lon = 128.0

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var isUserDragging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"바텀 내비바 숨깁니다")
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        init()
        initEvent()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.public_detail_map) as SupportMapFragment
        mapFragment.getMapAsync(readyCallback)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }

    private fun init(){
        // 좋아요 관련 로직
        //animateMoneyChange(leftMoney)
        viewModel.getGroupDetails(SharedPreferencesUtil.getAccessToken()!!, activityViewModel.storeId.value!!.toLong())
    }


    private fun initEvent() {
        binding.publicDetailTeamName.text = viewModel.detailInfo.value?.teamName
        Log.d(TAG, "initEvent: ${viewModel.detailInfo.value?.teamName}")
        binding.publicDetailText.text = viewModel.detailInfo.value?.teamMessage

        // 클릭 이벤트
        binding.likeBtn.setOnClickListener {
            heartCheck = !heartCheck
            toggle(heartCheck)
            val checkLike = LikeTeamsReq(activityViewModel.storeId.value!!.toLong(), heartCheck)
            sendlike(checkLike)
        }

        binding.qrPaymentBtn.setOnClickListener {
            lifecycleScope.launch {
                runCatching {
                    RetrofitUtil.qrService.qrPrivateCreate(SharedPreferencesUtil.getAccessToken()!!, activityViewModel.storeId.value!!.toInt())
                }.onSuccess {
                    Log.d(TAG, it.message)
                    showQRDialog(it.message + ":" + SharedPreferencesUtil.getAccessToken()!! + ":" + activityViewModel.storeId.value.toString())
                }.onFailure { e ->
                    Log.d(TAG, "qr실패: ${e.message}")
                    mainActivity.showToast("qr불러오기가 실패했습니다")
                }
            }
        }


        // 바텀시트 초기화 및 드래그만 허용하도록 설정
        val bottomSheet: View = requireView().findViewById(R.id.bottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)


        // 바텀 시트가 드래그 또는 클릭으로 상태가 변경되지 않도록 초기화
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setPeekHeight(
            430)

        // 바텀 시트를 클릭했을 때 상태를 변경하도록 설정
        bottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initViewModel() {
        viewModel.detailInfo.observe(viewLifecycleOwner) { it ->
            teamsRes = it
            Log.d(TAG, "initViewModel: ${it}")
            binding.publicDetailTeamName.text = teamsRes.teamName
            binding.publicDetailText.text = teamsRes.teamMessage
            binding.leftMoneyInfo.text =  "현재 잔액: "+CommonUtils.makeComma(teamsRes.teamBalance)
            binding.publicDetailLocation.text = teamsRes.address
            binding.dailyMoneyInfo.text = "개인 한도: "+CommonUtils.makeComma(teamsRes.dailyLimit-teamsRes.usedAmount)
            binding.storeName.text = teamsRes.storeName

            val imageUrl = teamsRes.imageUrl
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(Uri.parse(imageUrl)) // Uri로 변환 후 로드
                    .into(binding.publicDetailImage)
                Log.d(TAG, "이미지: ${imageUrl}")
            } else {
                binding.publicDetailImage.setImageResource(R.drawable.logo)
                Log.d(TAG, "initViewModel: ${imageUrl}")
            }

            Log.d(TAG, "initViewModel: ${teamsRes}")

            binding.publicDetailText.text = teamsRes.teamMessage
            val storeImageUrl = teamsRes.storeUrl
            if (!storeImageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(Uri.parse(storeImageUrl)) // Uri로 변환 후 로드
                    .into(binding.storeImage)
            } else {
                binding.storeImage.setImageResource(R.drawable.logo)
                Log.d(TAG, "initViewModel: ${imageUrl}")
            }

            lat = teamsRes.latitude
            lon = teamsRes.longitude
            heartCheck = teamsRes.checkLike
            toggle(heartCheck)
            // 숫자 값 관련 로직, 해당 숫자값은 받아올 수 있어야 함.
            val location = LatLng(lat, lon) // Location 대신 LatLng 사용
            Log.d(TAG,"위치 받아옵니다"+location.toString())
            setCurrentLocation(location, teamsRes.storeName, teamsRes.storeDescription)
            val leftMoney = teamsRes.usedAmount
        }
    }

    private fun toggle(heartCheck : Boolean){
        if (heartCheck) {
            binding.likeBtn.setImageResource(R.drawable.like_heart_fill)
        } else {
            binding.likeBtn.setImageResource(R.drawable.like_heart_empty)
        }
    }


    private val readyCallback: OnMapReadyCallback by lazy {
        object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                mMap = googleMap
                setDefaultLocation() // 초기 위치 설정
            }
        }
    }

    private fun setDefaultLocation() {
        val defaultLat = 36.1026  // 서울 중심 위도
        val defaultLon = 128.424  // 서울 중심 경도
        val defaultLocation = LatLng(defaultLat, defaultLon)

        val markerTitle = "기본 위치"
        val markerSnippet = "서울 중심"

        setCurrentLocation(defaultLocation, markerTitle, markerSnippet)

        // **초기 카메라 위치 설정**
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f)
        mMap?.moveCamera(cameraUpdate)  // 즉시 이동
    }

    private fun setCurrentLocation(
        latLng: LatLng,
        markerTitle: String?,
        markerSnippet: String?
    ) {
        currentMarker?.remove()

        val marker =
            ResourcesCompat.getDrawable(resources, R.drawable.location_icon, requireActivity().theme)
                ?.toBitmap(150, 150)

        val markerOptions = MarkerOptions().apply {
            position(latLng)
            title(markerTitle)
            snippet(markerSnippet)
            draggable(true)
            icon(BitmapDescriptorFactory.fromBitmap(marker!!))
        }

        currentMarker = mMap?.addMarker(markerOptions)
        val cameraUpdate = CameraUpdateFactory.newLatLng(latLng)
        mMap?.moveCamera(cameraUpdate)
    }

    // 숫자 증가 애니메이션
    private fun animateMoneyChange(targetAmount: Int) {
        val startAmount =
            binding.leftMoneyInfo.text.toString().replace(",", "").toIntOrNull() ?: 0
        val animator = ValueAnimator.ofInt(startAmount, targetAmount).apply {
            duration = 1500
            addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                val formattedValue =
                    NumberFormat.getNumberInstance(Locale.US).format(animatedValue)
                binding.leftMoneyInfo.text = formattedValue

            }
        }
        animator.start()
    }

    fun sendlike(likeTeamsReq: LikeTeamsReq) {
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.teamService.sendLikeStatus(
                    SharedPreferencesUtil.getAccessToken()!!,
                    likeTeamsReq
                )
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun showQRDialog(url: String = "https://www.naver.com") {
        val context = this@AddPublicGroupDetailsFragment.requireContext()

        // 타이머 생성
        val timer = Timer()

        // 다이얼로그 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_qr, null)

        // QR 코드 생성 및 이미지뷰에 적용
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = dialogView.findViewById<ImageView>(R.id.imageViewQrCode)
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QRDialog", "QR 코드 생성 실패", e)
        }

        // AlertDialog 생성
        val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)  // 뒤로 가기 버튼 허용
            .create()


        // 다이얼로그 닫힐 때 타이머 취소
        dialog.setOnDismissListener {
            timer.cancel()
            viewModel.getGroupDetails(SharedPreferencesUtil.getAccessToken()!!, activityViewModel.storeId.value!!.toLong())
        }

        // 다이얼로그 표시
        dialog.show()

        // 60초 카운트다운 타이머 시작
        var seconds = 60
        val qrTimer = dialogView.findViewById<TextView>(R.id.qr_timer)
        qrTimer?.text = "남은 시간: 60초"

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                seconds--
                // UI 업데이트는 메인 스레드에서 수행
                this@AddPublicGroupDetailsFragment.requireActivity().runOnUiThread {
                    qrTimer?.text = "남은 시간: ${seconds}초"
                }
                if (seconds <= 0) {
                    // 시간이 다 되었으면 다이얼로그를 닫고 타이머 취소
                    this@AddPublicGroupDetailsFragment.requireActivity().runOnUiThread {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                    timer.cancel()
                }
            }
        }, 1000, 1000)
    }
}
