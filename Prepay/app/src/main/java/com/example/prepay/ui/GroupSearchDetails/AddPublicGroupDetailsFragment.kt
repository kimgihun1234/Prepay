package com.example.prepay.ui.GroupSearchDetails

import android.animation.ValueAnimator
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.bumptech.glide.Glide
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.ui.MainActivityViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

private const val TAG = "PublicGroupDetailsFragment"

class AddPublicGroupDetailsFragment : BaseFragment<FragmentPublicGroupDetailsBinding>(
    FragmentPublicGroupDetailsBinding::bind,
    R.layout.fragment_public_group_details
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private val viewModel: GroupSearchtDetailsViewModel by viewModels()
    private val activityViewModel : MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.public_detail_map) as SupportMapFragment
        mapFragment.getMapAsync(readyCallback)
    }

    private fun initEvent() {

        viewModel.groupId.observe(viewLifecycleOwner, Observer { id ->
            binding.publicDetailQrBtn.isEnabled = id != null && id > 0
        })

        viewModel.groupName.observe(viewLifecycleOwner, Observer { name ->
            Log.d(TAG, "groupName updated: $name")
            binding.publicDetailTeamName.text = name
        })

        viewModel.groupMessage.observe(viewLifecycleOwner, Observer { message ->
            Log.d(TAG, "groupContent updated: $message")
            binding.publicDetailText.text = message
        })

        viewModel.groupLeftMoney.observe(viewLifecycleOwner, Observer { leftMoney ->
            Log.d(TAG, "groupLeftMoney updated: $leftMoney")
            //  binding.leftMoneyInfo.text = leftMoney.toString()
            animateMoneyChange(leftMoney)
        })

        viewModel.groupImageURL.observe(viewLifecycleOwner, Observer { imageURL ->
            Log.d(TAG, "groupImageURL updated: $imageURL")
            if (!imageURL.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(imageURL)
                    .into(binding.publicDetailImage)
            } else {
                binding.publicDetailImage.setImageResource(R.drawable.logo)
            }
        })

        // 좋아요 기능
        viewModel.isLiked.observe(viewLifecycleOwner, Observer { isLiked ->
            val imageRes = if (isLiked) R.drawable.like_heart_fill else R.drawable.like_heart_empty
            binding.likeBtn.setImageResource(imageRes)
        })


        binding.likeBtn.setOnClickListener {
            viewModel.toggleLike()
            val email = "user1@gmail.com"
            val info = activityViewModel.teamId.value?.let { LikeTeamsReq(it.toInt(), viewModel.isLiked.value ?: false) }
            Log.d(TAG, "onStop: ${activityViewModel.teamId.value}")
            info?.let { viewModel.sendLikeStatus(email, it) }
        }

        /**
         * QR 코드를 생성하여 다이얼로그로 표시하는 함수
         *
         * @param context 다이얼로그를 표시할 컨텍스트 (Fragment 내에서는 requireContext()를 사용)
         * @param url QR 코드에 인코딩할 URL (기본값: "https://www.naver.com")
         */
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

        binding.publicDetailQrBtn.setOnClickListener {
            lifecycleScope.launch {
                runCatching {
                    RetrofitUtil.qrService.qrTeamCreate("user1@gmail.com", 2)
                }.onSuccess {
                    Log.d(TAG,it.message)
                    showQRDialog(it.message)
                }.onFailure {
                    mainActivity.showToast("qr불러오기가 실패했습니다")
                }
            }
        }


        // GPT 코드
//        binding.publicDetailQrBtn.setOnClickListener {
//            viewModel.groupId.observe(viewLifecycleOwner) { groupId ->
//                groupId?.let { id ->
//                    lifecycleScope.launch {
//                        runCatching {
//                            RetrofitUtil.qrService.qrTeamCreate("user1@gmail.com", id)
//                        }.onSuccess {
//                            Log.d(TAG, it.message)
//                            showQRDialog(it.message)
//                        }.onFailure {
//                            mainActivity.showToast("qr불러오기가 실패했습니다")
//                        }
//                    }
//                } ?: mainActivity.showToast("그룹 ID가 없습니다.")
//            }
//        }

    }

    private val readyCallback: OnMapReadyCallback by lazy {
        object : OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                mMap = p0
                setDefaultLocation()
            }
        }
    }

    private fun setDefaultLocation() {
        val location = Location("")
        location.latitude = 37.56  // 서울 중심의 위도
        location.longitude = 126.97 // 서울 중심의 경도

        val markerTitle = "위치 정보 가져올 수 없음"
        val markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인 필요"

        setCurrentLocation(location, markerTitle, markerSnippet)
    }

    private fun setCurrentLocation(location: Location, markerTitle: String?, markerSnippet: String?) {
        currentMarker?.remove()

        val currentLatLng = LatLng(location.latitude + 0.002, location.longitude + 0.002)

        val marker = ResourcesCompat.getDrawable(resources, R.drawable.logo, requireActivity().theme)?.toBitmap(150, 150)

        val markerOptions = MarkerOptions().apply {
            position(currentLatLng)
            title("싸피벅스")
            snippet(markerSnippet)
            draggable(true)
            icon(BitmapDescriptorFactory.fromBitmap(marker!!))
        }

        currentMarker = mMap?.addMarker(markerOptions)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
        mMap?.animateCamera(cameraUpdate)
    }


    private fun getCurrentAddress(location: Location): String {
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        val addresses: List<Address>?

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: IOException) {
            Toast.makeText(requireActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 사용불가"
        } catch (e: IllegalArgumentException) {
            Toast.makeText(requireActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        return if (addresses.isNullOrEmpty()) {
            Toast.makeText(requireActivity(), "주소 발견 불가", Toast.LENGTH_LONG).show()
            "주소 발견 불가"
        } else {
            addresses[0].getAddressLine(0).toString()
        }
    }

    // 숫자 증가 애니메이션
    private fun animateMoneyChange(targetAmount: Int) {
        val startAmount = binding.leftMoneyInfo.text.toString().replace(",", "").toIntOrNull() ?: 0
        val animator = ValueAnimator.ofInt(startAmount, targetAmount).apply {
            duration = 1500
            addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                val formattedValue = NumberFormat.getNumberInstance(Locale.US).format(animatedValue)
                binding.leftMoneyInfo.text = formattedValue

            }
        }
        animator.start()
    }

//    override fun onStop() {
//        super.onStop()
//        val email = "user1@gmail.com"
//
//        val info = activityViewModel.teamId.value?.let { LikeTeamsReq(it, viewModel.isLiked.value ?: false) }
//        Log.d(TAG, "onStop: ${activityViewModel.teamId.value}")
//        info?.let { viewModel.sendLikeStatus(email, it) }
//    }
}
