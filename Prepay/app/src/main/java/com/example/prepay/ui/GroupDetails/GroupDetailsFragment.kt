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
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.model.dto.RestaurantData
import com.example.prepay.data.response.BanUserReq
import com.example.prepay.data.response.CodeRes
import com.example.prepay.data.response.MoneyChangeReq
import com.example.prepay.data.response.PrivilegeUserReq
import com.example.prepay.data.response.TeamIdReq
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.data.response.TeamUserRes
import com.example.prepay.databinding.DialogAuthoritySettingBinding
import com.example.prepay.databinding.DialogGroupExitBinding
import com.example.prepay.databinding.DialogGroupResignBinding
import com.example.prepay.databinding.DialogInviteCodeBinding
import com.example.prepay.databinding.DialogMoneyChangeBinding
import com.example.prepay.databinding.FragmentGroupDetailsBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsViewModel
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
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

private const val TAG = "GroupDetailsFragment_싸피"
class GroupDetailsFragment: BaseFragment<FragmentGroupDetailsBinding>(
    FragmentGroupDetailsBinding::bind,
    R.layout.fragment_group_details
), OnTeamUserActionListener{
    private lateinit var mainActivity: MainActivity

    private lateinit var teamUserAdapter: TeamUserAdapter
    private lateinit var teamTeamUserResList: List<TeamUserRes>
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    //activityViewModel
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewModel: GroupDetailsFragmentViewModel by viewModels()


    private var inviteCode = "0"
    private var codeMake = CodeRes(0,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // 부모 클래스의 onCreate 호출 (Fragment의 기본 동작 유지)
        mainActivity = context as MainActivity  // 현재 프래그먼트를 포함하는 액티비티(MainActivity)를 가져와 mainActivity 변수에 저장
        Log.d(TAG, activityViewModel.teamId.value.toString())

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu) // 메뉴 추가
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ic_menu -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END) // 👉 열려 있으면 닫기
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.END)  // 👉 닫혀 있으면 열기
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShow()
        initEvent()
        initViewModel()
        initDrawerLayout()
        initialView()
        initAdapter()
        /*requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }*/
        viewModel.getTeamDetail(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }

    private fun initShow(){
        mainActivity.changeFragmentGroupDetail(CommonUtils.GroupDetailFragmentName.GROUP_PREPAY_STORE_LIST_FRAGMENT)
    }


    private fun initAdapter(){
        teamTeamUserResList = emptyList()
        teamUserAdapter = TeamUserAdapter(teamTeamUserResList,this, false)
        binding.rvMemberList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMemberList.adapter = teamUserAdapter
    }

    private fun initViewModel(){
        viewModel.teamUserListInfo.observe(viewLifecycleOwner){it->
            teamUserAdapter.teamUserResList = it
            Log.d(TAG,it.toString())
            teamUserAdapter.notifyDataSetChanged()
        }
        viewModel.userposition.observe(viewLifecycleOwner){it->
            teamUserAdapter.updateUserPosition(it)
            Log.d(TAG,"업데이트가 되었습니다")
        }
        viewModel.getMyTeamRestaurantList(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
        viewModel.getMyTeamUserList(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!);

        viewModel.teamDetail.observe(viewLifecycleOwner){it->
            binding.groupDetailTitle.text = it.teamName
        }
    }

    private fun initDrawerLayout(){
        drawerLayout = binding.drawerLayout
        navigationView = binding.navigationView
    }

    private fun initEvent() {
        setButtonState(binding.prepayStoreListBtn, binding.listBtn)
        binding.prepayStoreListBtn.setOnClickListener {
            mainActivity.changeFragmentGroupDetail(CommonUtils.GroupDetailFragmentName.GROUP_PREPAY_STORE_LIST_FRAGMENT)
            setButtonState(binding.prepayStoreListBtn, binding.listBtn)
        }
        binding.listBtn.setOnClickListener {
            mainActivity.changeFragmentGroupDetail(CommonUtils.GroupDetailFragmentName.GROUP_PREPAY_HISTORY_FRAGMENT)
            setButtonState(binding.listBtn, binding.prepayStoreListBtn)
        }

        binding.groupInviteBtn.setOnClickListener {
            showInviteCodeInputDialog()
        }
        binding.groupExitBtn.setOnClickListener {
            showGroupExitDialog()
        }

        binding.moneyChangeBtn.setOnClickListener {
            showMoneyChangeDialog()
        }
        binding.detailHamburgerBtn.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }
    }

    fun changeMoneyView(){
        lifecycleScope.launch {
            kotlin.runCatching {
                RetrofitUtil.teamService.getTeamDetails(SharedPreferencesUtil.getAccessToken()!!, activityViewModel.teamId.value!!)
            }.onSuccess {
                Log.d(TAG,"얼마 사용"+it.dailyPriceLimit+" "+it.usedAmount)
                //binding.usePossiblePriceTxt.text = CommonUtils.makeComma(viewModel.moneyValue.value!!.toInt() - it.usedAmount)
                viewModel.updatePosition(it.position)
                inviteCode = (it.teamPassword ?: "초대코드없음").toString()
            }.onFailure {
                Log.d(TAG, "서버 데이터 가져오기 실패")
            }
        }
    }



    fun initialView(){
        lifecycleScope.launch{
            kotlin.runCatching {
                RetrofitUtil.teamService.getTeamDetails(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
            }.onSuccess {
                //binding.usePossiblePriceTxt.text = CommonUtils.makeComma(it.dailyPriceLimit-it.usedAmount)
                viewModel.updatePosition(it.position)
                binding.groupTitle.text = it.teamName
                inviteCode = (it.teamPassword ?: "초대코드없음").toString()
                if(it.position==false){
                    binding.moneyChangeBtn.visibility = View.GONE
                }
                if(it.position==true){
                    binding.groupInviteBtn.text = "코드 생성"
                }
            }.onFailure {
                Log.d(TAG,"실패하였습니다")
            }
        }
    }

    override fun onManageClick(teamUserRes: TeamUserRes) {
        showAuthoritySettingDialog(teamUserRes)
    }

    override fun onResignClick(teamUserRes: TeamUserRes) {
        showGroupResignDialog(teamUserRes)
    }


    //다이얼로그 보여주는 부분
    private fun showInviteCodeInputDialog() {
        val binding = DialogInviteCodeBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        Log.d(TAG,"초대코드"+inviteCode)
        binding.etInviteCode.text = inviteCode
        if(viewModel.userposition.value==false){
            binding.inviteCodeMakeBtn.visibility = View.GONE
            lifecycleScope.launch {
                val response = getCode(activityViewModel.teamId.value!!)
                if (response != null) {
                    Log.d(TAG, "결과값 성공: ${response.inviteCode}")
                    binding.etInviteCode.text = response.inviteCode
                } else {
                    Log.e(TAG, "초대 코드 생성 실패")
                }
            }
        }
        binding.inviteCodeMakeBtn.setOnClickListener {
            lifecycleScope.launch {
                val teamIdReq = TeamIdReq(activityViewModel.teamId.value!!.toInt())
                val response = makeCode(teamIdReq)
                if (response != null) {
                    Log.d(TAG, "결과값 성공: ${response.inviteCode}")
                    binding.etInviteCode.text = response.inviteCode
                } else {
                    Log.e(TAG, "초대 코드 생성 실패")
                }
            }
        }
        binding.inviteCodeConfirmBtn.setOnClickListener {
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


    private fun showMoneyChangeDialog() {
        val binding = DialogMoneyChangeBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.btnRegister.setOnClickListener {
            val moneyInput = binding.etCodeInput.text.toString()
            val moneyValue = if (moneyInput.isEmpty()) {
                10000
            } else {
                moneyInput.toInt()
            }
            val moneychange = MoneyChangeReq(moneyValue,activityViewModel.teamId.value!!.toInt())
            moneyChange(moneychange)
            Log.d(TAG,"실행됩니다")
            activityViewModel.setMoneyValue(moneychange.dailyPriceLimit)
            dialog.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showGroupResignDialog(ban: TeamUserRes) {
        val binding = DialogGroupResignBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.resignName.text = ban.nickname+"님을"
        binding.groupResignConfirmBtn.setOnClickListener {
            val banUser = BanUserReq(ban.email,ban.teamId)
            viewModel.TeamResign(SharedPreferencesUtil.getAccessToken()!!,banUser)
            dialog.dismiss()
        }

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAuthoritySettingDialog(privilege : TeamUserRes) {
        val binding = DialogAuthoritySettingBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
        binding.authoritySettingName.text = privilege.nickname+"님에게"
        binding.autoritySettingConfirmBtn.setOnClickListener {
            val pr = PrivilegeUserReq(privilege.email,true,privilege.teamId)
            privilegeUser(pr)
            showToast(privilege.nickname+"님에게 권한을 부여하였습니다.")
            dialog.dismiss()
            viewModel.getMyTeamUserList(SharedPreferencesUtil.getAccessToken()!!,activityViewModel.teamId.value!!)
        }

        binding.autoritySettingCancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private suspend fun makeCode(teamIdReq: TeamIdReq): CodeRes? {
        return withContext(Dispatchers.IO) {
            runCatching {
                RetrofitUtil.teamService.makeCode(
                    SharedPreferencesUtil.getAccessToken()!!,
                    teamIdReq
                )
            }.onSuccess { response ->
                return@withContext response
            }.onFailure { e ->
                Log.e(TAG, "API 호출 실패: ${e.message}", e)
            }
            return@withContext null // 실패 시 null 반환
        }
    }

    private suspend fun getCode(teamId: Long): CodeRes? {
        return withContext(Dispatchers.IO) {
            runCatching {
                RetrofitUtil.teamService.getCode(
                    SharedPreferencesUtil.getAccessToken()!!,
                    teamId
                )
            }.onSuccess { response ->
                return@withContext response
            }.onFailure { e ->
                Log.e(TAG, "API 호출 실패: ${e.message}", e)
            }
            return@withContext null // 실패 시 null 반환
        }
    }


    fun moneyChange(moneychange: MoneyChangeReq){
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.teamService.moneyChange(SharedPreferencesUtil.getAccessToken()!!,moneychange)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun privilegeUser(pr:PrivilegeUserReq){
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.teamService.privilegeUser(SharedPreferencesUtil.getAccessToken()!!,pr)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    fun exitTeam(tr: TeamIdReq){
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.teamService.exitTeam(SharedPreferencesUtil.getAccessToken()!!,tr)
            }.onSuccess {

            }.onFailure {

            }
        }
    }

    private fun setButtonState(activeButton: AppCompatButton, inactiveButton: AppCompatButton) {
        activeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.checked_color))
        inactiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.unchecked_color))
    }
}

