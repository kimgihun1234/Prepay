package com.example.prepay.ui.GroupSearch

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.SharedPreferencesUtil
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicLikeRes
import com.example.prepay.data.response.PublicTeamsDisRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.databinding.FragmentGroupSearchBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.util.KeyboardVisibilityUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import java.util.Objects

private const val TAG = "GroupSearchFragment"
class GroupSearchFragment: BaseFragment<FragmentGroupSearchBinding>(
    FragmentGroupSearchBinding::bind,
    R.layout.fragment_group_search
), OnPublicKmLimitClickListener,OnPublicLikeClickListener,OnPublicClickListener {
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    //adapter 정보
    private lateinit var publicDistanceSearchAdapter: PublicSearchDistanceAdapter
    private lateinit var publicLikeTeamAdapter : PublicSearchLikeAdapter
    private lateinit var publicGroupAdapter: PublicSearchAdapter

    // GPS관련 변수
    private var isUserLocationSet = false
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val groupSearchFragmentViewModel: GroupSearchFragmentViewModel by viewModels()

    /** permission check **/
    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var location = Location("dummy").apply {
        latitude = 36.107097
        longitude = 128.416369
    }
    private lateinit var currentLocation: Location

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    private var select = 1

    private var selectedButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity

//        val likeBtn: Button = findViewById
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
        //GPS 관련 코드
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermission()
        initViewModel()
        initAdapter()
        initEvent()
        keyboardVisibilityUtils = KeyboardVisibilityUtils(requireActivity().window,
            onShowKeyboard = {
                mainActivity.hideBottomNav(true) // 키보드 올라오면 숨김
            },
            onHideKeyboard = {
                mainActivity.hideBottomNav(false) // 키보드 내려가면 다시 보이게
            }
        )

        // 처음 활성화 버튼
        selectedButton = binding.all

        // searchBar 클릭 이벤트
        val searchBar = binding.searchRestaurant
        searchBar.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Log.d(TAG, "Focus gained")
                view.setBackgroundResource(R.drawable.focus_shape_alll_round)
            } else {
                view.setBackgroundResource(R.drawable.search_bar_shape)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        keyboardVisibilityUtils.detachKeyboardListeners()
    }

    private fun initEvent() {
        select = 3
        binding.recyclerView.adapter = publicGroupAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getLastLocation()
        binding.likeSort.setOnClickListener {
            select = 1
            handleButtonClick(it as Button)
            //groupSearchFragmentViewModel.getTeamLikeList()
            binding.recyclerView.adapter = publicLikeTeamAdapter
            getLastLocation()
        }

        binding.distanceSort.setOnClickListener {
            select = 2
            handleButtonClick(it as Button)
            binding.recyclerView.adapter = publicDistanceSearchAdapter
            getLastLocation()
            Log.d(TAG, "initEvent: ${groupSearchFragmentViewModel.sortDistancePublicTeams.value}")
        }

        binding.all.setOnClickListener {
            select = 3
            handleButtonClick(it as Button)
            binding.recyclerView.adapter = publicGroupAdapter
            getLastLocation()
        }
        
         binding.searchRestaurant.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                query?.let { filterSearchResults(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                if (newText.isNullOrEmpty() || newText=="") {
                    binding.recyclerView.isVisible = true
                    updateAdapterWithOriginalList()
                } else {
                    binding.recyclerView.isVisible = true
                    filterSearchResults(newText)
                }
                return false
            }

            private fun updateAdapterWithOriginalList() {
                when (select) {
                    1 -> groupSearchFragmentViewModel.getPublicLikeTeams.value?.let {
                        Log.d(TAG, "publicGroupAdapter: $it")
                        publicLikeTeamAdapter.publiclikeList = it
                        publicLikeTeamAdapter.notifyDataSetChanged()
                    }
                    2 -> groupSearchFragmentViewModel.sortDistancePublicTeams.value?.let{
                        Log.d(TAG, "publicDistanceSearchAdapter: $it")
                        publicDistanceSearchAdapter.publicGroupList = it
                        publicDistanceSearchAdapter.notifyDataSetChanged()
                    }
                    else -> groupSearchFragmentViewModel.getPublicTeams.value?.let {
                        Log.d(TAG, "publicLikeTeamAdapter: $it")
                        publicGroupAdapter.publicGroupList = it
                        publicGroupAdapter.notifyDataSetChanged()
                    }
                }
            }


            private fun filterSearchResults(newText: String) {
                val originalList = getOriginalList()

                val filterList = originalList.filter { searchable ->
                    searchable.searchableText.contains(newText, ignoreCase = true)
                }
                updateAdapterBasedOnSelection(filterList)

            }

            private fun updateAdapterBasedOnSelection(filterList: List<Searchable>) {
                when (select) {
                    1-> {
                        publicLikeTeamAdapter.publiclikeList = filterList as List<PublicLikeRes>
                        publicLikeTeamAdapter.notifyDataSetChanged()
                    }
                    2-> {
                        publicDistanceSearchAdapter.publicGroupList = filterList as List<PublicTeamsDisRes>
                        publicDistanceSearchAdapter.notifyDataSetChanged()
                    }
                    else-> {
                        publicGroupAdapter.publicGroupList = filterList as List<PublicTeamsRes>
                        publicGroupAdapter.notifyDataSetChanged()
                    }
                }
            }

            private fun getOriginalList(): List<Searchable> {
                return when(select) {
                    1 -> groupSearchFragmentViewModel.getPublicLikeTeams.value ?: emptyList()
                    2 -> groupSearchFragmentViewModel.sortDistancePublicTeams.value ?: emptyList()
                    else -> groupSearchFragmentViewModel.getPublicTeams.value ?: emptyList()
                }
            }

        })
    }


    private fun initAdapter(){
        publicLikeTeamAdapter = PublicSearchLikeAdapter(arrayListOf(),this)
        publicDistanceSearchAdapter = PublicSearchDistanceAdapter(arrayListOf(),this)
        publicGroupAdapter = PublicSearchAdapter(arrayListOf(),this)
    }

    private fun initViewModel() {
        groupSearchFragmentViewModel.userLocation.observe(viewLifecycleOwner) { curlocation ->
            // 위치 정보가 변경될 때마다 호출
            Log.d(TAG,"변화"+curlocation.toString())
            if(select ==1){
                groupSearchFragmentViewModel.getTeamLikeList(curlocation.latitude, curlocation.longitude)
            }
            else if(select==2){
                groupSearchFragmentViewModel.getSortDistancePublicTeamList(curlocation.latitude, curlocation.longitude)
            }
            else{
                groupSearchFragmentViewModel.getPublicTeamList(curlocation.latitude, curlocation.longitude)
            }
        }

        groupSearchFragmentViewModel.sortDistancePublicTeams.observe(viewLifecycleOwner){it->
            Log.d(TAG,it.toString())
            publicDistanceSearchAdapter.publicGroupList = it
            Log.d(TAG,"결과 값"+it.toString())
            publicDistanceSearchAdapter.notifyDataSetChanged()
        }
        groupSearchFragmentViewModel.getPublicLikeTeams.observe(viewLifecycleOwner){it->
            publicLikeTeamAdapter.publiclikeList = it
            Log.d(TAG,"결과 값"+it.toString())
            publicLikeTeamAdapter.notifyDataSetChanged()
        }
        groupSearchFragmentViewModel.getPublicTeams.observe(viewLifecycleOwner){it->
            publicGroupAdapter.publicGroupList = it
            publicGroupAdapter.notifyDataSetChanged()
        }
    }
    fun sendlike(likeTeamsReq: LikeTeamsReq){
        lifecycleScope.launch {
           runCatching {
               RetrofitUtil.teamService.sendLikeStatus(SharedPreferencesUtil.getAccessToken()!!, likeTeamsReq)
           }.onSuccess {
               Log.d(TAG, "sendlike: 성공")
           }.onFailure { e ->
               Log.d(TAG, "sendlike: ${e.message}")
           }
       }
    }


    override fun onKmLimitGroupClick(publicgroup: PublicTeamsDisRes) {
        activityViewModel.setStoreId(publicgroup.teamId)
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT)
    }

    override fun onPublicGroupLikeClick(publicGroupLike: PublicLikeRes) {
        activityViewModel.setStoreId(publicGroupLike.teamId)
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT)
    }

    override fun onGroupClick(publicgroup: PublicTeamsRes) {
        activityViewModel.setStoreId(publicgroup.teamId)
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT)
    }

    override fun onLikeClick(publicgroupLike: LikeTeamsReq) {
        sendlike(publicgroupLike)
    }

    override fun onKmLimitLikeGroupClick(likeReq: LikeTeamsReq) {
        sendlike(likeReq)
    }

    override fun onPublicGroupLikeLikeClick(publiclike:LikeTeamsReq){
        sendlike(publiclike)
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
    private val readyCallback: OnMapReadyCallback by lazy {
        object : OnMapReadyCallback {
            override fun onMapReady(p0: GoogleMap) {
                setDefaultLocation()
            }
        }
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
                    groupSearchFragmentViewModel.updateLocation(location)
                }
                Log.d(TAG,"시작"+location.toString())
                //현재 위치에 마커 생성하고 이동

            }
        }
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

    fun setCurrentLocation(location: Location){
        val markerTitle: String = getCurrentAddress(location)
        val markerSnippet = "위도: ${location.latitude.toString()}, 경도: ${location.longitude }"

    }
    fun setCurrentLocation(location: Location, markerTitle: String?, markerSnippet: String?) {

    }

    private fun getLastLocation() {
        if (checker.checkPermission(requireActivity(), runtimePermissions)) {
            mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "최근 위치 가져오기 성공: ${location.latitude}, ${location.longitude}")
                    groupSearchFragmentViewModel.updateLocation(location)
                    //isUserLocationSet = true // 중복 업데이트 방지
                } else {
                    Log.d(TAG, "최근 위치 정보가 없음, 새로 요청 필요")
                    startLocationUpdates() // 최신 위치 요청
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "최근 위치 가져오기 실패: ${e.message}")
                startLocationUpdates() // 최신 위치 요청
            }
        } else {
            Log.d(TAG, "위치 권한 없음")
        }
    }

    private fun handleButtonClick(clickedButton: Button) {
        // 이전에 선택된 버튼이 있다면 기본 스타일로 초기화합니다.
        selectedButton?.apply {
            setBackgroundResource(R.drawable.filter_btn) // 기본 배경 리소스
            setTextColor(ContextCompat.getColor(context, R.color.black)) // 기본 텍스트 색상
        }
        // 새로 클릭한 버튼에 클릭된 스타일을 적용합니다.
        clickedButton.apply {
            Log.d(TAG, "ㅇㅇㅇㅇㅇㅇㅇ${clickedButton}")
            setBackgroundResource(R.drawable.clicked_filter_btn) // 클릭된 상태의 배경 리소스
            setTextColor(ContextCompat.getColor(context, R.color.white)) // 클릭된 상태의 텍스트 색상
        }

        // 선택된 버튼을 업데이트합니다.
        selectedButton = clickedButton
    }



    /******** 위치서비스 활성화 여부 check *********/
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private var needRequest = false


}

