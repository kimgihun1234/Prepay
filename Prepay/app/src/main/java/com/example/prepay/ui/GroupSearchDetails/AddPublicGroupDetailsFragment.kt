package com.example.prepay.ui.GroupSearchDetails

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.DialogQrDiningTogetherBinding
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
import com.bumptech.glide.Glide
import com.example.prepay.data.model.dto.Public
import java.io.IOException
import java.util.Locale

private const val TAG = "PublicGroupDetailsFragment"

class AddPublicGroupDetailsFragment : BaseFragment<FragmentPublicGroupDetailsBinding>(
    FragmentPublicGroupDetailsBinding::bind,
    R.layout.fragment_public_group_details
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null

    // 리사이클러뷰 선택한 팀의 정보 가지고 오기
    companion object {
        fun newInstance(publicgroup: Public): AddPublicGroupDetailsFragment {
            return AddPublicGroupDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", publicgroup.pk)
                    putString("name", publicgroup.name)
                    putString("address", publicgroup.address)
                    putInt("distance", publicgroup.distance)
                    putInt("leftMoney", publicgroup.leftMoney)
                    putString("imageURL", publicgroup.imageURL)
                }
            }
        }
    }

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
//        val groupDetail = ViewModelProvider(requireActivity()).get(GroupSearchtDetailsViewModel::class.java)
//
//        Log.d(com.example.prepay.ui.GroupSearchDetails.TAG, "initEvent: ${groupDetail.groupDetailsData.value}")
//        binding.publicDetailTeamName.text = groupDetail.groupDetailsData.value
        val groupName = arguments?.getString("name") ?: "그룹명 없음"
        val groupLeftMoney = arguments?.getInt("leftMoney") ?: 0
        val groupImageURL = arguments?.getString("imageURL") ?: ""

        binding.publicDetailTeamName.text = groupName

        // 숫자 증가 애니메이션
        animateMoneyChange(groupLeftMoney)

        if (!groupImageURL.isNullOrEmpty()) {
            // Glide 또는 Picasso 등을 사용하여 이미지를 로드할 수 있습니다.
            Glide.with(requireContext())
                .load(groupImageURL)
                .into(binding.publicDetailImage)
        } else {
            binding.publicDetailImage.setImageResource(R.drawable.logo) // 기본 이미지 설정
        }

        // QR 다이얼로그 부분
        binding.publicDetailQrBtn.setOnClickListener {
            val dialogBinding = DialogQrDiningTogetherBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogBinding.root)
                .create()
            dialog.show()

            // 다이얼로그 외부를 터치시 닫음
            dialog.setCanceledOnTouchOutside(true)
        }
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
        val startAmount = 0
        val animator = ValueAnimator.ofInt(startAmount, targetAmount).apply {
            duration = 2000 // 애니메이션 시간 (2초)
            addUpdateListener { valueAnimator ->
                val animatedValue = valueAnimator.animatedValue as Int
                binding.leftMoneyInfo.text = animatedValue.toString()
            }
        }
        animator.start()
    }
}
