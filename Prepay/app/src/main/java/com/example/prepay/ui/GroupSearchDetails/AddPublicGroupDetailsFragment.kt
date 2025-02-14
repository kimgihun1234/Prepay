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
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamDetailsRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.ui.GroupSearch.GroupSearchFragmentViewModel
import com.example.prepay.ui.GroupSearch.PublicSearchAdapter
import com.example.prepay.ui.MainActivityViewModel
//import com.google.zxing.BarcodeFormat
//import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.coroutineScope
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
    private val viewModel: GroupSearchDetailsViewModel by viewModels()
    private val activityViewModel : MainActivityViewModel by activityViewModels()
    private lateinit var teamsRes: PublicTeamDetailsRes


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initEvent()
        initViewModel()
        
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.public_detail_map) as SupportMapFragment
        mapFragment.getMapAsync(readyCallback)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }


    private fun initViewModel() {
        viewModel.detailInfo.observe(viewLifecycleOwner) { it ->
            teamsRes = it
            binding.publicDetailTeamName.text = it.teamName
            binding.publicDetailText.text = it.teamMessage
            Log.d(TAG, "initViewModel: ${it}")

            val imageURL = it.imageURL
            if (!imageURL.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(imageURL)
                    .into(binding.publicDetailImage)
            } else {
                binding.publicDetailImage.setImageResource(R.drawable.logo)
            }

            // 숫자 값 관련 로직, 해당 숫자값은 받아올 수 있어야 함.
            val leftMoney = it.usedAmount
            animateMoneyChange(leftMoney)

            // 좋아요 초기 상태 반영
            viewModel.isLiked.observe(viewLifecycleOwner) { isLiked ->
                binding.likeBtn.setImageResource(
                    if (isLiked) R.drawable.like_heart_fill else R.drawable.like_heart_empty
                )
            }

            // 클릭 이벤트
            binding.likeBtn.setOnClickListener {
                val newLikeStatus = !(viewModel.isLiked.value ?: false)
                viewModel.toggleLike()

                val likeRequest = LikeTeamsReq(6, newLikeStatus)
                viewModel.sendLikeStatus("user1@gmail.com", likeRequest)
            }

        }
        viewModel.getGroupDetails("user1@gmail.com", 6)
    }

    private fun initEvent() {
        binding.publicDetailTeamName.text = viewModel.detailInfo.value?.teamName
        Log.d(TAG, "initEvent: ${viewModel.detailInfo.value?.teamName}")
        binding.publicDetailText.text = viewModel.detailInfo.value?.teamMessage
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

    fun sendlike(likeTeamsReq: LikeTeamsReq){
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.teamService.sendLikeStatus(SharedPreferencesUtil.getAccessToken()!!,likeTeamsReq)
            }.onSuccess {

            }.onFailure {

            }
        }
    }
}
