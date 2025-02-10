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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.BanUserReq
import com.example.prepay.data.response.PrivilegeUserReq
import com.example.prepay.data.response.TeamIdReq
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.data.response.TeamUserRes
import com.example.prepay.databinding.DialogAuthoritySettingBinding
import com.example.prepay.databinding.DialogGroupExitBinding
import com.example.prepay.databinding.DialogGroupResignBinding
import com.example.prepay.databinding.DialogInviteCodeBinding
import com.example.prepay.databinding.DialogQrDiningTogetherBinding
import com.example.prepay.databinding.FragmentGroupDetailsBinding
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
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale

private const val TAG = "GroupDetailsFragment_싸피"
class GroupDetailsFragment: BaseFragment<FragmentGroupDetailsBinding>(
    FragmentGroupDetailsBinding::bind,
    R.layout.fragment_group_details
), RestaurantAdapter.OnRestaurantClickListener, OnTeamUserActionListener{
    private lateinit var mainActivity: MainActivity
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var teamUserAdapter: TeamUserAdapter
    private lateinit var restaurantList: List<TeamIdStoreRes>
    private lateinit var teamTeamUserResList: List<TeamUserRes>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var currentLocation: Location
    //activityViewModel
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailsFragmentViewModel by viewModels()

    //GPS관련 변수
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var isUserLocationSet = false
    private var location = Location("dummy").apply {
        latitude = 36.107097
        longitude = 128.416369
    }
    private var userposition = true

    /** permission check **/
    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity
        Log.d(TAG, activityViewModel.teamId.value.toString())
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
        initData()
        initDrawerLayout()
        initModelView()
        //GPS 관련 코드
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(readyCallback)
    }

    private fun initAdapter(){
        teamTeamUserResList = emptyList()
        restaurantList = emptyList()
        restaurantAdapter = RestaurantAdapter(restaurantList,this,location)
        teamUserAdapter = TeamUserAdapter(teamTeamUserResList,this, userposition)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = restaurantAdapter
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemberList.adapter = teamUserAdapter
        viewModel.storeListInfo.observe(viewLifecycleOwner){ it->
            restaurantAdapter.teamIdStoreResList = it
            restaurantList = it
            if (mMap != null) {
                addStoreMarkers(it) // 마커 추가
            }
            restaurantAdapter.notifyDataSetChanged()
        }
        viewModel.teamUserListInfo.observe(viewLifecycleOwner){it->
            teamUserAdapter.teamUserResList = it
            teamUserAdapter.notifyDataSetChanged()
        }
        viewModel.userposition.observe(viewLifecycleOwner){it->
            teamUserAdapter.userposition = it
            teamUserAdapter.notifyDataSetChanged()
        }
        viewModel.getMyTeamRestaurantList(1,activityViewModel.teamId.value!!)
        viewModel.getMyTeamUserList(1,activityViewModel.teamId.value!!);

        viewModel.userLocation.observe(viewLifecycleOwner) { curlocation ->
            // 위치 정보가 변경될 때마다 호출
            Log.d(TAG,"변화"+curlocation.toString())
            restaurantAdapter.userLocation = curlocation
            restaurantAdapter.notifyDataSetChanged()
        }
    }

    private fun initData(){


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

    private fun addStoreMarkers(stores: List<TeamIdStoreRes>) {
        mMap!!.clear()  // 기존 마커 삭제

        for (store in stores) {
            val storeLocation = LatLng(store.latitude, store.longitude)

            val markerOptions = MarkerOptions().apply {
                position(storeLocation)
                title(store.storeName)
                snippet("위도: ${store.latitude}, 경도: ${store.longitude}")
            }
            mMap!!.addMarker(markerOptions)
        }

        // 첫 번째 상점 위치로 카메라 이동
        if (stores.isNotEmpty()) {
            val firstStoreLocation = LatLng(stores[0].latitude, stores[0].longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(firstStoreLocation, 15f)
            mMap!!.animateCamera(cameraUpdate)
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

        binding.qrBtn.setOnClickListener {
            mainActivity.broadcast("hello","hello")
        }
    }

    private fun addRestaurantClick() {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.ADD_RESTAURANT_FRAGMENT)
    }

    override fun onRestaurantClick(teamIdStoreResId: Int) {
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
    }

    override fun onManageClick(teamUserRes: TeamUserRes) {
        showAuthoritySettingDialog(teamUserRes)
    }

    override fun onResignClick(teamUserRes: TeamUserRes) {
        showGroupResignDialog(teamUserRes)
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
            val tr = TeamIdReq(teamId = activityViewModel.teamId.value!!.toInt())
            exitTeam(tr)
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
            dialog.dismiss()
        }

        binding.groupExitCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showGroupResignDialog(ban: TeamUserRes) {
        val binding = DialogGroupResignBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.groupResignConfirmBtn.setOnClickListener {
            val banUser = BanUserReq(ban.email,ban.teamId)
            viewModel.TeamResign(banUser)
            dialog.dismiss()
        }

        binding.groupResignCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAuthoritySettingDialog(privilege : TeamUserRes) {
        val binding = DialogAuthoritySettingBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.autoritySettingConfirmBtn.setOnClickListener {
            val pr = PrivilegeUserReq(privilege.email,true,privilege.teamId)
            privilegeUser(pr)
            dialog.dismiss()
        }

        binding.autoritySettingCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun privilegeUser(pr:PrivilegeUserReq){
        lifecycleScope.launch {
            runCatching {
              RetrofitUtil.teamService.privilegeUser(1,pr)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun exitTeam(tr: TeamIdReq){
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.teamService.exitTeam(1,tr)
            }.onSuccess {

            }.onFailure {

            }
        }
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
                viewModel.storeListInfo.value?.let { addStoreMarkers(it) }
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
                if(!isUserLocationSet){
                    //여기에 추가
                    isUserLocationSet = true
                    viewModel.updateLocation(location)
                }
                Log.d(TAG,"시작"+location.toString())
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

        // 첫 번째 마커 위치
        val currentLatLng1 = LatLng(location.latitude + 0.002, location.longitude + 0.002)
        val marker1 = ResourcesCompat.getDrawable(resources, R.drawable.logo, requireActivity().theme)?.toBitmap(150, 150)

        // 마커 옵션 1
        val markerOptions1 = MarkerOptions().apply {
            position(currentLatLng1)
            title("싸피벅스")
            snippet(markerSnippet)
            draggable(true)
            icon(BitmapDescriptorFactory.fromBitmap(marker1!!))
        }

        // 첫 번째 마커 추가
        mMap?.addMarker(markerOptions1)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng1, 15f)
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

    fun initModelView(){
        lifecycleScope.launch{
            kotlin.runCatching {
                RetrofitUtil.teamService.getTeamDetails(1,activityViewModel.teamId.value!!)
            }.onSuccess {
                binding.usePossiblePriceTxt.text = it.dailyPriceLimit.toString()
                viewModel.updatePosition(it.position)
                Log.d(TAG,"숫자 출려"+it.position.toString())
            }.onFailure {
                Log.d(TAG,"실패하였습니다")
            }
        }
    }
}

