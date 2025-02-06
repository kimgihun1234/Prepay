package com.example.prepay.ui.GroupDetails

import android.Manifest
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
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.User
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.databinding.DialogAuthoritySettingBinding
import com.example.prepay.databinding.DialogGroupExitBinding
import com.example.prepay.databinding.DialogGroupResignBinding
import com.example.prepay.databinding.DialogInviteCodeBinding
import com.example.prepay.databinding.DialogQrDiningTogetherBinding
import com.example.prepay.databinding.FragmentGroupDetailsBinding
import com.example.prepay.ui.MainActivity
import javax.sql.DataSource
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsFragment
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
import com.google.android.material.navigation.NavigationView
import java.io.IOException
import java.util.Locale

class GroupDetailsFragment: BaseFragment<FragmentGroupDetailsBinding>(
    FragmentGroupDetailsBinding::bind,
    R.layout.fragment_group_details
), RestaurantAdapter.OnRestaurantClickListener, OnTeamUserActionListener{
    private lateinit var mainActivity: MainActivity
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var teamUserAdapter: TeamUserAdapter
    private lateinit var restaurantList: List<Restaurant>
    private lateinit var teamUserList: List<User>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    //GPS관련 변수
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapter()
        initDrawerLayout()
        //GPS 관련 ㅋ코드
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(readyCallback)
    }

    private fun initAdapter(){
        restaurantList = listOf(
            Restaurant("꿀맛 식당", 10000),
            Restaurant("싸피 식당", 20000),
            Restaurant("삼성 식당", 4000)
        )
        teamUserList = listOf(
            User("김싸피","ㅇㅇㅇㄹ","ㅇㅇㅇ"),
            User("김ㄷㄷㄷ","ㄹㄹㄹ","ㅇㅇㅇ"),
            User("김ㄹㄹ","ㄱㄱㄱ","ㅇㅇㅇ")
        )
        restaurantAdapter = RestaurantAdapter(restaurantList,this)
        teamUserAdapter = TeamUserAdapter(teamUserList,this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = restaurantAdapter
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemberList.adapter = teamUserAdapter
    }

    private fun initDrawerLayout(){
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
        drawerLayout.openDrawer(GravityCompat.END)
        binding.drawerLayout.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }


    private fun initEvent() {
        binding.diningTogetherQrBtn.setOnClickListener {
            showQrCodeDialog()
        }

        binding.groupInviteBtn.setOnClickListener {
            showInviteCodeInputDialog()
        }
        binding.groupExitBtn.setOnClickListener {
            showGroupExitDialog()
        }
        binding.addRestaurant.setOnClickListener {
            addRestaurantClick()
        }
    }

    private fun addRestaurantClick() {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.ADD_RESTAURANT_FRAGMENT)
    }

    override fun onRestaurantClick(restaurant: Restaurant) {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
    }

    override fun onManageClick(user: User) {
        showAuthoritySettingDialog()
    }

    override fun onResignClick(user: User) {
        showGroupResignDialog()
    }

    private fun showQrCodeDialog(){
        val binding = DialogQrDiningTogetherBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        dialog.show()
    }



    private fun showInviteCodeInputDialog() {
        val binding = DialogInviteCodeBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        binding.inviteCodeConfirmBtn.setOnClickListener {
            val code = binding.etInviteCode.text.toString()
            if (code.isNotEmpty()) {
                Toast.makeText(requireContext(), "코드 입력: $code", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.inviteCodeCancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showGroupExitDialog() {
        val binding = DialogGroupExitBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.groupExitConfirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.groupExitCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showGroupResignDialog() {
        val binding = DialogGroupResignBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.groupResignConfirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.groupResignCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAuthoritySettingDialog() {
        val binding = DialogAuthoritySettingBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.autoritySettingConfirmBtn.setOnClickListener {
            dialog.dismiss()
        }

        binding.autoritySettingCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private val readyCallback: OnMapReadyCallback by lazy{
        object: OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                mMap = p0

                //퍼미션 요청 대화상자 (권한이 없을때) & 실행 시 초기 위치를 서울 중심부로 이동
                setDefaultLocation()

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
                /** permission check **/
                mMap?.setOnMapLongClickListener {
                    val loc = Location("")
                    loc.latitude = it.latitude
                    loc.longitude = it.longitude
                    setCurrentLocation(loc)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        // 위치서비스 활성화 여부 check
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            if (checker.checkPermission(requireActivity(), runtimePermissions)) {
                mMap?.isMyLocationEnabled = true
                mMap?.uiSettings?.isZoomControlsEnabled = true
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

    //위치정보 요청시 호출
    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location)
            }
        }
    }

    fun setCurrentLocation(location: Location){
        val markerTitle: String = getCurrentAddress(location)
        val markerSnippet = "위도: ${location.latitude.toString()}, 경도: ${location.longitude }"

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet)
    }

    fun setCurrentLocation(location: Location, markerTitle: String?, markerSnippet: String?) {
        currentMarker?.remove()

        val currentLatLng = LatLng(location.latitude+0.002, location.longitude+0.002)

        val marker = ResourcesCompat.getDrawable(resources,R.drawable.logo,requireActivity().theme)?.toBitmap(150,150)

        val markerOptions = MarkerOptions().apply{
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

    fun getCurrentAddress(location: Location): String {
        //지오코더: GPS를 주소로 변환
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(requireActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(requireActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        return if (addresses == null || addresses.isEmpty()) {
            Toast.makeText(requireActivity(), "주소 발견 불가", Toast.LENGTH_LONG).show()
            "주소 발견 불가"
        } else {
            val address = addresses[0]
            address.getAddressLine(0).toString()
        }
    }

    private fun setDefaultLocation() {

        //초기 위치를 서울로
        val location = Location("")
        location.latitude = 37.56
        location.longitude = 126.97

        val markerTitle = "위치정보 가져올 수 없음"
        val markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인 필요"

        if(checker.checkPermission(requireActivity(),runtimePermissions)){
            mFusedLocationClient.lastLocation.addOnSuccessListener {
                setCurrentLocation(it)
            }
        } else{
            setCurrentLocation(location,markerTitle,markerSnippet)
        }
    }

    /** 권한 관련 **/
    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }


    /******** 위치서비스 활성화 여부 check *********/
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private var needRequest = false

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS를 켰는지 검사함
                if (checkLocationServicesStatus()) {
                    needRequest = true
                    return
                }else{
                    Toast.makeText(requireActivity(),
                        "위치 서비스가 꺼져 있어, 현재 위치를 확인할 수 없습니다.",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }
}

