package com.example.prepay.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.prepay.ApplicationClass
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.remote.FirebaseTokenService
import com.example.prepay.data.response.SignInTeamReq
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.databinding.ActivityMainBinding
import com.example.prepay.databinding.DialogVisitCodeBinding
import com.example.prepay.ui.CreateGroup.CreateGroupFragment
import com.example.prepay.ui.CreateGroup.CreatePrivateGroupFragment
import com.example.prepay.ui.CreateGroup.CreatePublicGroupFragment
import com.example.prepay.ui.GroupDetails.AddRestaurantFragment
import com.example.prepay.ui.GroupDetails.GroupDetailsFragment
import com.example.prepay.ui.GroupSearch.GroupSearchFragment
import com.example.prepay.ui.GroupSearch.GroupSearchFragmentViewModel
import com.example.prepay.ui.GroupSearch.OnPublicClickListener
import com.example.prepay.ui.GroupSearch.PublicSearchAdapter
import com.example.prepay.ui.GroupSearchDetails.AddPublicGroupDetailsFragment
import com.example.prepay.ui.MyPage.MyPageFragment
import com.example.prepay.ui.RestaurantDetails.AddDetailRestaurantFragment
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val KeyAccessToken = "AccessToken"
private val prefsName = "user_prefs"

private const val TAG = "MainActivity_싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val groupSearchFragmentViewModel : GroupSearchFragmentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
        initEvent()
        setSupportActionBar(binding.toolbar)
        setupToolbarListener()
    }

    fun changeFragmentMain(name: CommonUtils.MainFragmentName, num: Int = -1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (name) {
            CommonUtils.MainFragmentName.MYPAGE_FRAGMENT -> {
                transaction.replace(R.id.main_container, MyPageFragment())
            }
            CommonUtils.MainFragmentName.GROUP_SEARCH_FRAGMENT -> {
                transaction.replace(R.id.main_container, GroupSearchFragment())
            }
            CommonUtils.MainFragmentName.CREATE_GROUP_FRAGMENT -> {
                transaction.replace(R.id.main_container, CreateGroupFragment())
                transaction.addToBackStack(null)
            }
            CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT -> {
                transaction.replace(R.id.main_container, GroupDetailsFragment())
                transaction.addToBackStack(null)
            }
            CommonUtils.MainFragmentName.LOOK_GROUP_FRAGMENT -> TODO()
            CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT -> {
                transaction.replace(R.id.main_container, RestaurantDetailsFragment())
                transaction.addToBackStack(null)
            }
            CommonUtils.MainFragmentName.ADD_RESTAURANT_FRAGMENT -> {
                transaction.replace(R.id.main_container, AddRestaurantFragment())
            }
            CommonUtils.MainFragmentName.DETAIL_RESTAURANT_FRAGMENT -> {
                transaction.replace(R.id.main_container, AddDetailRestaurantFragment())
            }
            CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT -> {
                transaction.replace(R.id.main_container, AddPublicGroupDetailsFragment())
            }
        }
        transaction.commit()
    }

    fun changeFragmentGroup(name: CommonUtils.GroupFragmentName, num: Int = -1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (name) {
            CommonUtils.GroupFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT -> {
                transaction.replace(R.id.create_group_container, CreatePublicGroupFragment())
            }
            CommonUtils.GroupFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT -> {
                transaction.replace(R.id.create_group_container, CreatePrivateGroupFragment())
            }
        }
        transaction.commit()
    }

    fun initFragment(){
        changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
    }

    fun hideBottomNav(state : Boolean){
        if(state) binding.bottomNavigation.visibility = View.GONE
        else binding.bottomNavigation.visibility = View.VISIBLE
    }

    //툴바 관련 코드
    private fun setupToolbarListener() {
        supportFragmentManager.setFragmentResultListener("toolbarUpdate", this) { _, bundle ->
            val isHamburger = bundle.getBoolean("showHamburger", false)
            updateToolbarIcon(isHamburger)
        }
    }

    private fun updateToolbarIcon(showHamburger: Boolean) {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(showHamburger)
            if (showHamburger) {
                setHomeAsUpIndicator(R.drawable.ic_menu)
            }
        }
    }

    fun enterDialog(){
        val binding = DialogVisitCodeBinding.inflate(LayoutInflater.from(this))
        // AlertDialog 생성
        val dialog = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        // '취소' 버튼 클릭 리스너
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        // '등록' 버튼 클릭 리스너
        binding.btnRegister.setOnClickListener {
            val code = binding.etCodeInput.text.toString()
            val requestBody = SignInTeamReq(code)
            // Context가 있는 곳 (예: Activity나 Fragment)
            val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            val accessToken = sharedPref.getString(KeyAccessToken, "") ?: ""
            Log.d(TAG, "불러온 AccessToken: $accessToken")

            lifecycleScope.launch {
                try {
                    val response = RetrofitUtil.teamService.signInTeam(
                        access = accessToken,
                        request = requestBody
                    )
                    // 요청이 성공했을 때만 대화상자 닫기
                    dialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "초대 코드를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }

    fun initEvent() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_mypage -> {
                    changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                    true
                }

                R.id.navigation_group_search -> {
                    changeFragmentMain(CommonUtils.MainFragmentName.GROUP_SEARCH_FRAGMENT)
                    true
                }
                else -> false
            }
        }
    }

    fun broadcast(title: String,body: String){
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000) // 10초(10000ms) 대기
            val storeService = ApplicationClass.retrofit.create(FirebaseTokenService::class.java)
            storeService.broadcast(title,body).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful){
                        val res = response.body()
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        Log.d(TAG, "onResponse: Error Code ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }
    }

    companion object{

    }
}