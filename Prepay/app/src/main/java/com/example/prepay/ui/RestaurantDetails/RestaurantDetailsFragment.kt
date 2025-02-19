package com.example.prepay.ui.RestaurantDetails

import android.Manifest
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.databinding.DialogReceiptBinding
import com.example.prepay.databinding.FragmentRestaurantDetailsBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "RestaurantDetailsFragme_싸피"
class RestaurantDetailsFragment: BaseFragment<FragmentRestaurantDetailsBinding>(
    FragmentRestaurantDetailsBinding::bind,
    R.layout.fragment_restaurant_details
){
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: RestaurantDetailsViewModel by viewModels()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private val GPS_ENABLE_REQUEST_CODE = 2001

    /** permission check **/
    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity
    }


    override fun onStart() {
        super.onStart()
        if (checker.checkPermission(requireActivity(), runtimePermissions)) {
            startLocationUpdates()
        }
    }

    override fun onStop() {
        super.onStop()
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initEvent()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.restaurant_map) as SupportMapFragment
        mapFragment.getMapAsync(readyCallback)
    }
    private fun initEvent() {
        Log.d(TAG,"시작되었다."+activityViewModel.teamId.value!!.toInt()+" "+activityViewModel.storeId.value!!)
        binding.restaurantNameBootpay.text = activityViewModel.storeName.value
        viewModel.getTeamStoreDetail(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!.toInt(),activityViewModel.storeId.value!!)
        binding.payBootpay.setOnClickListener {
            val storeId = activityViewModel.storeId
            val storeName = activityViewModel.storeName
            Log.d(TAG, "storeId, storeName : $storeId, $storeName")
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.DETAIL_RESTAURANT_FRAGMENT)
        }
    }

    private fun initViewModel() {
        viewModel.teamStoreDetail.observe(viewLifecycleOwner){it->
            Log.d(TAG,"결과 값"+it.toString())
            binding.restaurantNameBootpay.text = it.storeName
            binding.restaurantAddress.text = it.storeDescription
            binding.totalAmount.text = CommonUtils.makeComma(it.balance)
            Glide.with(binding.root.context)
                .load(it.storeImgUrl)
                // 이미지 로드중 로드 실패시에는 로고 띄워줌
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.restaurantImage)
            val location = LatLng(it.latitude, it.longitude)
            setCurrentLocation(location,it.storeName,it.storeDescription)
        }
    }



    private fun requestPermission() {
        /** permission check **/
        if (!checker.checkPermission(requireActivity(), runtimePermissions)) {
            checker.setOnGrantedListener {
                //퍼미션 획득 성공일때
                startLocationUpdates()
            }
            checker.requestPermissionLauncher.launch(runtimePermissions)
        } else { //이미 전체 권한이 있는 경우
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        // 위치서비스 활성화 여부 check
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            if (checker.checkPermission(requireActivity(), runtimePermissions)) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null)
            }
        }
    }

    private val locationRequest: LocationRequest by lazy{
        LocationRequest.create().apply {
            interval = 1000   // 1초
            fastestInterval = 500  // 0.5초
            smallestDisplacement = 10.0f   //10m
        }
    }

    /** 권한 관련 **/
    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    private fun showDialogForLocationServiceSetting() {
        val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소"
        ) { dialog, _ -> dialog.cancel() }
        builder.create().show()
    }

    //위치정보 요청시 호출
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                /*if(!isUserLocationSet){
                    //여기에 추가
                    isUserLocationSet = true
                    groupSearchFragmentViewModel.updateLocation(location)
                }
                Log.d(com.example.prepay.ui.GroupSearch.TAG,"시작"+location.toString())*/
                //현재 위치에 마커 생성하고 이동

            }
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

        val currentLatLng = LatLng( latLng.latitude,  latLng.longitude)

        val marker =
            ResourcesCompat.getDrawable(resources, R.drawable.location_icon, requireActivity().theme)
                ?.toBitmap(150, 150)

        val markerOptions = MarkerOptions().apply {
            position(currentLatLng)
            title(markerTitle)
            snippet(markerSnippet)
            draggable(true)
            icon(BitmapDescriptorFactory.fromBitmap(marker!!))
        }

        currentMarker = mMap?.addMarker(markerOptions)
        val cameraUpdate = CameraUpdateFactory.newLatLng(latLng)
        mMap?.moveCamera(cameraUpdate)
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

}