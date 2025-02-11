package com.example.prepay.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.prepay.ApplicationClass
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.remote.FirebaseTokenService
import com.example.prepay.databinding.ActivityMainBinding
import com.example.prepay.databinding.DialogVisitCodeBinding
import com.example.prepay.ui.CreateGroup.CreatePrivateGroupFragment
import com.example.prepay.ui.CreateGroup.CreatePublicGroupFragment
import com.example.prepay.ui.GroupDetails.AddRestaurantFragment
import com.example.prepay.ui.GroupDetails.GroupDetailsFragment
import com.example.prepay.ui.GroupSearch.GroupSearchFragment
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

private const val TAG = "MainActivity_싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    
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
            CommonUtils.MainFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT -> {
                transaction.replace(R.id.main_container, CreatePrivateGroupFragment())
            }
            CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT -> {
                transaction.replace(R.id.main_container, CreatePublicGroupFragment())
                transaction.addToBackStack(null)
            }
            CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT -> {
                transaction.replace(R.id.main_container, GroupDetailsFragment())
                transaction.addToBackStack(null)
            }
            CommonUtils.MainFragmentName.LOOK_GROUP_FRAGMENT -> TODO()
            CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT -> {
                transaction.replace(R.id.main_container, RestaurantDetailsFragment())
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
            val code = binding.etCodeInput.toString()
            // 코드 등록 로직 추가
            dialog.dismiss()
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