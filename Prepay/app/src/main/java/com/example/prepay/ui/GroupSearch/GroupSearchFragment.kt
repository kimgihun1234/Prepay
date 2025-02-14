package com.example.prepay.ui.GroupSearch

import android.Manifest
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
import android.widget.Button
import android.widget.Toast
import com.example.prepay.ui.MainActivityViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsDisRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.databinding.FragmentGroupSearchBinding
import com.example.prepay.ui.GroupSearchDetails.AddPublicGroupDetailsFragment
import com.example.prepay.ui.GroupSearchDetails.GroupSearchDetailsViewModel
import com.example.prepay.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import kotlin.math.log

private const val TAG = "GroupSearchFragment"
class GroupSearchFragment: BaseFragment<FragmentGroupSearchBinding>(
    FragmentGroupSearchBinding::bind,
    R.layout.fragment_group_search
), PublicSearchDistanceAdpater.OnClickLinstener, OnPublicClickListener {
    private lateinit var mainActivity: MainActivity
    private lateinit var publicSearchAdapter: PublicSearchAdapter
    private val viewModel: GroupSearchFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var publicSearchDistanceAdpater: PublicSearchDistanceAdpater


    // GPS관련 변수
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val groupSearchFragmentViewModel: GroupSearchFragmentViewModel by viewModels()

    /** permission check **/
    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var isUserLocationSet = false
    private var location = Location("dummy").apply {
        latitude = 36.107097
        longitude = 128.416369
    }
    private lateinit var currentLocation: Location

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


        initAdapter()

        //GPS 관련 코드

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermission()
        initEvent()
        initAdapter()
        initViewModel()
    }

    private fun initEvent() {
        binding.distanceSort.setOnClickListener {

            publicSearchDistanceAdpater = PublicSearchDistanceAdpater(arrayListOf(),this)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = publicSearchDistanceAdpater
            groupSearchFragmentViewModel.sortDistancePublicTeams.observe(viewLifecycleOwner) { it ->
                publicSearchDistanceAdpater.publicGroupList = it
                publicSearchDistanceAdpater.notifyDataSetChanged()
            }

            groupSearchFragmentViewModel.getSortDistancePublicTeamList(groupSearchFragmentViewModel.userLocation.value!!.latitude, groupSearchFragmentViewModel.userLocation.value!!.longitude)
            Log.d(TAG, "initEvent: ${groupSearchFragmentViewModel.userLocation.value!!.latitude}")
            Log.d(TAG, "initEvent: ${groupSearchFragmentViewModel.userLocation.value!!.longitude}")
            Log.d(TAG, "initEvent: ${groupSearchFragmentViewModel.getSortDistancePublicTeamList(groupSearchFragmentViewModel.userLocation.value!!.latitude, groupSearchFragmentViewModel.userLocation.value!!.longitude)}")
        }
    }

    private fun initAdapter(){

        publicSearchAdapter = PublicSearchAdapter(arrayListOf(),this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = publicSearchAdapter
        groupSearchFragmentViewModel.getPublicTeams.observe(viewLifecycleOwner){it->
            publicSearchAdapter.publicGroupList = it
            publicSearchAdapter.notifyDataSetChanged()
        }

//        // 임시로 코드 작성
//        val email = "user1@gmail.com"
        groupSearchFragmentViewModel.getAllPublicTeamList()
    }

    private fun initViewModel() {
        groupSearchFragmentViewModel.userLocation.observe(viewLifecycleOwner) { curlocation ->
            // 위치 정보가 변경될 때마다 호출
            Log.d(TAG,"변화"+curlocation.toString())
        }
    }

    override fun onGroupClick(publicgroup: PublicTeamsRes) {
        Log.d(TAG, "onGroupClick: ")
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT)
    }

    override fun onLikeClick(publicgroup: LikeTeamsReq) {
        Log.d(TAG,"클릭하였습니다")
        sendlike(publicgroup)
    }

    fun sendlike(likeTeamsReq: LikeTeamsReq){
       lifecycleScope.launch {
           runCatching {
               RetrofitUtil.teamService.sendLikeStatus("user1@gmail.com",likeTeamsReq)
           }.onSuccess {

           }.onFailure {

           }
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


    /******** 위치서비스 활성화 여부 check *********/
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private var needRequest = false


    override fun onGroupClick(publicgroup: PublicTeamsDisRes) {
        TODO("Not yet implemented")
    }

}

