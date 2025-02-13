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
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.databinding.FragmentGroupSearchBinding
import com.example.prepay.ui.GroupDetails.GroupDetailsFragmentViewModel
import com.example.prepay.ui.GroupSearch.PublicSearchAdapter.OnPublicClickListener
import com.example.prepay.ui.GroupSearchDetails.AddPublicGroupDetailsFragment
import com.example.prepay.ui.GroupSearchDetails.GroupSearchtDetailsViewModel
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
import java.io.IOException
import java.util.Locale

private const val TAG = "GroupSearchFragment"
class GroupSearchFragment: BaseFragment<FragmentGroupSearchBinding>(
    FragmentGroupSearchBinding::bind,
    R.layout.fragment_group_search
), OnPublicClickListener {
    private lateinit var mainActivity: MainActivity
    private lateinit var publicSearchAdapter: PublicSearchAdapter

    // GPS관련 변수
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val groupSearchFragmentViewModel: GroupSearchFragmentViewModel by viewModels()
    /** permission check **/
    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity

        val likeBtn: Button = findViewById
    }

    override fun onStart() {
        super.onStart()

        if (checker.checkPermission(requireActivity(), runtimePermissions)) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        //GPS 관련 코드
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun initAdapter(){
//        publicList = listOf(
//            Public(1, "A 카페", "대구광역시 동성로",10, 1000000, "https://fastly.picsum.photos/id/221/200/300.jpg?hmac=vFrrajnPFCrr5ttjepVTsUDWzoo-orpnXOsqdqAd0LU"),
//            Public(2, "B 카페", "구미시 진평동", 20, 500000, "https://fastly.picsum.photos/id/875/200/300.jpg?hmac=9NSoqXHP89pGlq4Sz3OgGxjx5c91YHJkcIOBFgNJ8xA"),
//            Public(2, "C 카페", "서울특별시 강남구", 30,800000, "https://fastly.picsum.photos/id/729/200/300.jpg?hmac=VbcZBxFYzQK1ro1MTLLmwHNQ0kuIJSagOeue4JMymUY")
//        )
//        publicAdapter = PublicSearchAdapter(arrayListOf(),this)
//        viewModel.getPublicTeams.observe(viewLifecycleOwner){it->
//            publicAdapter.publicGroupList = it
//            publicAdapter.notifyDataSetChanged()
//        }

        publicSearchAdapter = PublicSearchAdapter(arrayListOf(),this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = publicSearchAdapter
        groupSearchFragmentViewModel.getPublicTeams.observe(viewLifecycleOwner){it->
            publicSearchAdapter.publicGroupList = it
            publicSearchAdapter.notifyDataSetChanged()
        }

        // 임시로 코드 작성
        val email = "user1@gmail.com"
        groupSearchFragmentViewModel.getAllPublicTeamList()
    }

    override fun onGroupClick(publicgroup: PublicTeamsRes) {
        val viewModel = ViewModelProvider(mainActivity).get(GroupSearchtDetailsViewModel::class.java)
        viewModel.sendGroupDetails(
            publicgroup.teamId,
            publicgroup.teamName,
            publicgroup.teamMessage,
            publicgroup.teamBalance,
            publicgroup.teamInitializerNickname,
            publicgroup.imageURL
        )

        val fragment = AddPublicGroupDetailsFragment()
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT)
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

