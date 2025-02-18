package com.example.prepay.ui

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.activity.viewModels
import com.example.prepay.ApplicationClass
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.remote.FirebaseTokenService
import com.example.prepay.data.response.SignInTeamReq
import com.example.prepay.data.response.TokenReq
import com.example.prepay.databinding.ActivityMainBinding
import com.example.prepay.databinding.DialogVisitCodeBinding
import com.example.prepay.ui.CreateGroup.CreateGroupFragment
import com.example.prepay.ui.CreateGroup.CreatePrivateGroupFragment
import com.example.prepay.ui.CreateGroup.CreatePublicGroupFragment
import com.example.prepay.ui.GroupDetails.AddRestaurantFragment
import com.example.prepay.ui.GroupDetails.GroupDetailsFragment
import com.example.prepay.ui.GroupDetails.GroupPaymentHistoryFragment
import com.example.prepay.ui.GroupDetails.GroupPrepayStoreListFragment
import com.example.prepay.ui.GroupSearch.GroupSearchFragment
import com.example.prepay.ui.GroupSearch.GroupSearchFragmentViewModel
import com.example.prepay.ui.GroupSearchDetails.AddPublicGroupDetailsFragment
import com.example.prepay.ui.MyPage.MyPageFragment
import com.example.prepay.ui.RestaurantDetails.AddDetailRestaurantFragment
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsFragment
import com.example.prepay.ui.Notification.NotificationFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
        initEvent()
        init()
        setupToolbarListener()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setBackgroundColor(android.graphics.Color.TRANSPARENT)
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
            CommonUtils.MainFragmentName.NOTIFICATION_FRAGMENT -> {
                transaction.replace(R.id.main_container, NotificationFragment())
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
                transaction.addToBackStack(null)
            }
            CommonUtils.MainFragmentName.DETAIL_RESTAURANT_FRAGMENT -> {
                transaction.replace(R.id.main_container, AddDetailRestaurantFragment())
            }
            CommonUtils.MainFragmentName.PUBLIC_GROUP_DETAILS_FRAGMENT -> {
                transaction.replace(R.id.main_container, AddPublicGroupDetailsFragment())
                transaction.addToBackStack(null)
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

    fun changeFragmentGroupDetail(name: CommonUtils.GroupDetailFragmentName, num: Int = -1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (name) {
            CommonUtils.GroupDetailFragmentName.GROUP_PREPAY_STORE_LIST_FRAGMENT -> {
                transaction.replace(R.id.group_details_container, GroupPrepayStoreListFragment())
            }
            CommonUtils.GroupDetailFragmentName.GROUP_PREPAY_HISTORY_FRAGMENT -> {
                transaction.replace(R.id.group_details_container,GroupPaymentHistoryFragment())
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
                R.id.my_payment_history -> {
                    changeFragmentMain(CommonUtils.MainFragmentName.NOTIFICATION_FRAGMENT)
                    true
                }
                else -> false
            }
        }
    }

//    override fun onBackPressed() {
//        val fragment = supportFragmentManager.findFragmentByTag(MyPageFragment::class.java.simpleName)
//        if (fragment != null) {
//            Log.d(TAG, "true: 뒤로가기 처리")
//            // MyPageFragment로 돌아가도록 처리
//            changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
//        } else {
//            // 기본적으로 Activity의 뒤로 가기 처리
//            Log.d(TAG, "else: 뒤로가기 처리")
//            super.onBackPressed()
//        }
//    }

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

    private fun init(){
        initFCM()
        createNotificationChannel(channel_id, "ssafy")
    }

    private fun initFCM(){
        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                val tr = TokenReq(task.result!!)
                uploadToken(tr)
            }
        })
    }

    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(id: String, name: String){
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(id, name, importance))
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        const val channel_id = "ssafy_channel"

        // ratrofit  수업 후 network 에 업로드 할 수 있도록 구성
        fun uploadToken(token: TokenReq) {
            // 새로운 토큰 수신 시 서버로 전송
            val storeService = ApplicationClass.retrofit.create(FirebaseTokenService::class.java)
            storeService.uploadToken(SharedPreferencesUtil.getAccessToken()!!,token).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        Log.d(TAG, "로그인 onResponse: Error Code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }
    }
}