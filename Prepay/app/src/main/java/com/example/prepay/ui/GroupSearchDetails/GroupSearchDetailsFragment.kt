//package com.example.prepay.ui.GroupSearchDetails
//
//import android.Manifest
//import android.content.Context.LOCATION_SERVICE
//import android.content.Intent
//import android.location.Address
//import android.location.Geocoder
//import android.location.Location
//import android.location.LocationManager
//import android.os.Bundle
//import android.provider.Settings
//import android.view.View
//import android.widget.Toast
//import androidx.core.content.res.ResourcesCompat
//import androidx.core.graphics.drawable.toBitmap
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.prepay.BaseFragment
//import com.example.prepay.CommonUtils
//import com.example.prepay.PermissionChecker
//import com.example.prepay.R
//import com.example.prepay.data.model.dto.Public
//import com.example.prepay.databinding.FragmentGroupSearchBinding
//import com.example.prepay.ui.GroupDetails.GroupDetailsFragment
//import com.example.prepay.ui.GroupSearch.PublicSearchAdapter
//import com.example.prepay.ui.MainActivity
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationRequest
//import com.google.android.gms.location.LocationResult
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.material.navigation.NavigationView
//import java.io.IOException
//import java.util.Locale
//
//class GroupSearchDetailsFragment: BaseFragment<FragmentGroupSearchBinding>(
//    FragmentGroupSearchBinding::bind,
//    R.layout.fragment_group_search
//), PublicSearchAdapter.OnPublicClickListener {
//    private lateinit var mainActivity: MainActivity
//    private lateinit var publicSearchAdapter: PublicSearchAdapter
//    private lateinit var publicList: List<Public>
//
//    //GPS관련 변수
//    private var mMap: GoogleMap? = null
//    private var currentMarker: Marker? = null
//    private lateinit var mFusedLocationClient: FusedLocationProviderClient
//
//    /** permission check **/
//    private val checker = PermissionChecker(this)
//    private val runtimePermissions = arrayOf(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mainActivity= context as MainActivity
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        if (checker.checkPermission(requireActivity(), runtimePermissions)) {
//            startLocationUpdates()
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mFusedLocationClient.removeLocationUpdates(locationCallback)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initAdapter()
//        //GPS 관련 코드
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(readyCallback)
//    }
//
//    private fun initAdapter(){
//        publicList = listOf(
//            Public(1, "A 카페", "대구광역시 동성로",10, "https://fastly.picsum.photos/id/221/200/300.jpg?hmac=vFrrajnPFCrr5ttjepVTsUDWzoo-orpnXOsqdqAd0LU"),
//            Public(2, "B 카페", "구미시 진평동", 20, "https://fastly.picsum.photos/id/875/200/300.jpg?hmac=9NSoqXHP89pGlq4Sz3OgGxjx5c91YHJkcIOBFgNJ8xA"),
//            Public(3, "C 카페", "서울특별시 강남구", 30,"https://fastly.picsum.photos/id/729/200/300.jpg?hmac=VbcZBxFYzQK1ro1MTLLmwHNQ0kuIJSagOeue4JMymUY")
//        )
//        publicSearchAdapter = PublicSearchAdapter(publicList, this)
//
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerView.adapter = publicSearchAdapter
//    }
//
//    override fun onGroupClick(publicgroup: Public) {
////        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
//        val bundle = Bundle()
//        bundle.putint("pk", publicgroup.pk)
//        val fragment = GroupDetailsFragment()
//        fragment.arguments = bundle
//
//    }
//
//
//    private val readyCallback: OnMapReadyCallback by lazy{
//        object: OnMapReadyCallback {
//            override fun onMapReady(p0: GoogleMap) {
//                mMap = p0
//
//                //퍼미션 요청 대화상자 (권한이 없을때) & 실행 시 초기 위치를 서울 중심부로 이동
//                setDefaultLocation()
//
//                /** permission check **/
//                if (!checker.checkPermission(requireActivity(), runtimePermissions)) {
//                    checker.setOnGrantedListener {
//                        //퍼미션 획득 성공일때
//                        startLocationUpdates()
//                    }
//                    checker.requestPermissionLauncher.launch(runtimePermissions)
//                } else { //이미 전체 권한이 있는 경우
//                    startLocationUpdates()
//                }
//                /** permission check **/
//                mMap?.setOnMapLongClickListener {
//                    val loc = Location("")
//                    loc.latitude = it.latitude
//                    loc.longitude = it.longitude
//                    setCurrentLocation(loc)
//                }
//            }
//        }
//    }
//
//    private fun startLocationUpdates() {
//        // 위치서비스 활성화 여부 check
//        if (!checkLocationServicesStatus()) {
//            showDialogForLocationServiceSetting()
//        } else {
//            if (checker.checkPermission(requireActivity(), runtimePermissions)) {
//                mMap?.isMyLocationEnabled = true
//                mMap?.uiSettings?.isZoomControlsEnabled = true
//                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,null)
//            }
//        }
//    }
//
//    private val locationRequest: LocationRequest by lazy{
//        LocationRequest.create().apply {
//            interval = 1000   // 1초
//            fastestInterval = 500  // 0.5초
//            smallestDisplacement = 10.0f   //10m
//        }
//    }
//
//    //위치정보 요청시 호출
//    var locationCallback: LocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            super.onLocationResult(locationResult)
//            val locationList = locationResult.locations
//            if (locationList.size > 0) {
//                val location = locationList[locationList.size - 1]
//
//                //현재 위치에 마커 생성하고 이동
//                setCurrentLocation(location)
//            }
//        }
//    }
//
//    fun setCurrentLocation(location: Location){
//        val markerTitle: String = getCurrentAddress(location)
//        val markerSnippet = "위도: ${location.latitude.toString()}, 경도: ${location.longitude }"
//
//        //현재 위치에 마커 생성하고 이동
//        setCurrentLocation(location, markerTitle, markerSnippet)
//    }
//
//    fun setCurrentLocation(location: Location, markerTitle: String?, markerSnippet: String?) {
//        currentMarker?.remove()
//
//        val currentLatLng = LatLng(location.latitude+0.002, location.longitude+0.002)
//
//        val marker = ResourcesCompat.getDrawable(resources,R.drawable.logo,requireActivity().theme)?.toBitmap(150,150)
//
//        val markerOptions = MarkerOptions().apply{
//            position(currentLatLng)
//            title("싸피벅스")
//            snippet(markerSnippet)
//            draggable(true)
//            icon(BitmapDescriptorFactory.fromBitmap(marker!!))
//        }
//
//        currentMarker = mMap?.addMarker(markerOptions)
//
//        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
//        mMap?.animateCamera(cameraUpdate)
//    }
//
//    fun getCurrentAddress(location: Location): String {
//        //지오코더: GPS를 주소로 변환
//        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
//        val addresses: List<Address>?
//        try {
//            addresses = geocoder.getFromLocation(
//                location.latitude,
//                location.longitude,
//                1
//            )
//        } catch (ioException: IOException) {
//            //네트워크 문제
//            Toast.makeText(requireActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
//            return "지오코더 사용불가"
//        } catch (illegalArgumentException: IllegalArgumentException) {
//            Toast.makeText(requireActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
//            return "잘못된 GPS 좌표"
//        }
//
//        return if (addresses == null || addresses.isEmpty()) {
//            Toast.makeText(requireActivity(), "주소 발견 불가", Toast.LENGTH_LONG).show()
//            "주소 발견 불가"
//        } else {
//            val address = addresses[0]
//            address.getAddressLine(0).toString()
//        }
//    }
//
//    private fun setDefaultLocation() {
//
//        //초기 위치를 서울로
//        val location = Location("")
//        location.latitude = 37.56
//        location.longitude = 126.97
//
//        val markerTitle = "위치정보 가져올 수 없음"
//        val markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인 필요"
//
//        if(checker.checkPermission(requireActivity(),runtimePermissions)){
//            mFusedLocationClient.lastLocation.addOnSuccessListener {
//                setCurrentLocation(it)
//            }
//        } else{
//            setCurrentLocation(location,markerTitle,markerSnippet)
//        }
//    }
//
//    /** 권한 관련 **/
//    private fun checkLocationServicesStatus(): Boolean {
//        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
//        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//    }
//
//
//    /******** 위치서비스 활성화 여부 check *********/
//    private val GPS_ENABLE_REQUEST_CODE = 2001
//    private var needRequest = false
//
//    private fun showDialogForLocationServiceSetting() {
//        val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
//        builder.setTitle("위치 서비스 비활성화")
//        builder.setMessage(
//            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
//        )
//        builder.setCancelable(true)
//        builder.setPositiveButton("설정") { _, _ ->
//            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
//        }
//        builder.setNegativeButton("취소"
//        ) { dialog, _ -> dialog.cancel() }
//        builder.create().show()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            GPS_ENABLE_REQUEST_CODE ->
//                //사용자가 GPS를 켰는지 검사함
//                if (checkLocationServicesStatus()) {
//                    needRequest = true
//                    return
//                }else{
//                    Toast.makeText(requireActivity(),
//                        "위치 서비스가 꺼져 있어, 현재 위치를 확인할 수 없습니다.",
//                        Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//}
//
