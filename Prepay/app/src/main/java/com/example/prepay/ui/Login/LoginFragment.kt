package com.example.prepay.ui.Login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.databinding.FragmentLoginBinding
import com.example.prepay.response.KakaoLoginRequest
import com.example.prepay.response.LoginRequest
import com.example.prepay.ui.LoginActivity
import com.example.prepay.ui.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment_싸피"
class LoginFragment: BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::bind,
    R.layout.fragment_login
){
    private lateinit var loginActivity: LoginActivity

    //구글 로그인
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    // SharedPreferences 파일명 및 키 정의
    private val prefsName = "user_prefs"
    private val keyUserId = "userId"
    private val keyUserPw = "userPw"
    private val KeyAccessToken = "AccessToken"

    private lateinit var editTexts: List<EditText>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
        //구글 로그인
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SharedPreferences를 통한 자동 로그인 체크
        SharedPreferencesUtil.init(requireContext())
        // 저장된 아이디와 비밀번호 불러오기
        val (savedId, savedPw) = SharedPreferencesUtil.getUserCredentials()

        // 저장된 아이디와 패스워드가 모두 있다면, 자동 로그인 API 호출
        if (!savedId.isNullOrEmpty() && !savedPw.isNullOrEmpty()) {
            Log.d(TAG, "자동 로그인 실행")
            login(savedId, savedPw)
            return
        }


        // editView list
        editTexts = listOf(
            view.findViewById(R.id.login_id_text),
            view.findViewById(R.id.log_in_password_text)
        )

        // 이벤트
        initEvent()

        // 스타일 관련 함수
        setStyleCatchphrase(view)
        initFocusChangeListener()
        setUpTextWatcher()

    }

    fun initEvent(){

        binding.JoinBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.SIGNIN_FRAGMENT)
        }
        binding.findPasswordBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.FINDPASSWORD_FRAGMENT)
        }
        binding.googleLoginBtn.setOnClickListener {
            goSignup()
        }
        binding.backBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.START_LOGIN_FRAGMENT)
        }
        // --- login 관련 기능 ---
        binding.LoginBtn.setOnClickListener {
            login()
        }
        // 카카오 로그인 버튼 클릭 처리 (예: testButton)
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
        binding.autoIdCheckbox.setOnClickListener { view ->
            val autoButton = view.findViewById<CheckBox>(R.id.auto_id_checkbox)
            if (autoButton.isChecked) {
                // 체크된 상태일 때의 색상 설정
                autoButton.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(), R.color.checked_color
                )
            } else {
                // 체크 해제 상태일 때의 색상 설정
                autoButton.buttonTintList = ContextCompat.getColorStateList(
                    requireContext(), R.color.unchecked_color
                )
            }
        }
    }

    // 캐치프라이 스타일 지정
    private fun setStyleCatchphrase(view: View) {
        val text = "선결제의 새로운 바람"  // 전체 문자열 정의
        val spannableString = SpannableString(text)  // SpannableString 객체 생성

        // "바람" 부분의 시작 인덱스와 끝 인덱스 계산
        val target = "바람"
        val start = text.indexOf(target)
        val end = start + target.length

        // 색상 적용 (예: #0066CC)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#0066CC")),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // 굵기 적용 (Bold)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // 텍스트 크기 적용
        spannableString.setSpan(
            AbsoluteSizeSpan(20, true),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // TextView에 설정
        val catchphrase: TextView = view.findViewById(R.id.catchphrase)
        catchphrase.text = spannableString
    }
    // editView focus 이벤트 설정
    private fun initFocusChangeListener() {

        editTexts.forEach {
            it.setOnFocusChangeListener { _, isFocus ->
                if (isFocus) {
                    it.setBackgroundResource(R.drawable.focus_shape_alll_round)
                } else {
                    it.setBackgroundResource(R.drawable.shape_all_round)
                }
            }
        }
    }
    // TextView 크기 변환 이벤트
    private fun setUpTextWatcher() {
        editTexts.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty()) {
                        it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    } else {
                        it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    checkSubmitButtonState()
                }
            })
        }
    }

    // 모든 EditText가 채워졌는지 확인하여 submit 버튼의 색상을 변경하는 함수
    private fun checkSubmitButtonState() {
        // 모든 EditText의 텍스트가 비어있지 않은지 체크
        val isAllFilled = editTexts.all { it.text.toString().trim().isNotEmpty() }

        // 채워졌다면 활성화 색상, 아니라면 비활성화 색상 적용 (예: 활성화 시 #0066CC, 미완료 시 회색)
        binding.LoginBtn.apply {
            isEnabled = isAllFilled
            setBackgroundResource(
                if (isAllFilled)
                    R.drawable.submit_button_style
                else
                    R.drawable.disable_button_style // 비활성화 색상
            )
        }
    }
    // 로그인 함수
    private fun login(id: String? = null, password: String? = null) {
        // 파라미터가 전달되면 해당 값을 사용하고, 그렇지 않으면 사용자가 입력한 값을 사용
        val userId = id ?: binding.loginIdText.text.toString().trim()
        val userPw = password ?: binding.logInPasswordText.text.toString().trim()

        if (userId.isEmpty() || userPw.isEmpty()) {
            Toast.makeText(requireContext(), "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            runCatching {
                val loginRequest = LoginRequest(userId, userPw)
                RetrofitUtil.userService.login(loginRequest)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val accessToken = response.headers()["access"]
                    if (accessToken != null) {
                        // Access Token 저장
                        SharedPreferencesUtil.saveAccessToken(accessToken)
                        Log.d(TAG, "Access Token 저장됨: $accessToken")
                    }
                    if (binding.autoIdCheckbox.isChecked) {
                        // 사용자 정보 저장
                        SharedPreferencesUtil.saveUserCredentials(userId, userPw)
                        Log.d(TAG, "사용자 정보 저장됨: $userId")
                    }
                    Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "로그인 실패: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.onFailure {
                Toast.makeText(requireContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.w("LoginFragment", "Google sign in failed", e)
                Toast.makeText(requireContext(), "구글 로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()  // 로그인 후 이전 액티비티 종료
                } else {
                    Log.w("LoginFragment", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "구글 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goSignup() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        // Notification Channel ID
    }

    private fun kakaoLogin() {
        // 카카오계정 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공: ${token.accessToken}")
                // 서버에 accessToken 전달
                performKakaoLogin(token.accessToken)
            }
        }
        // 카카오톡이 설치되어 있다면 카카오톡으로 로그인 시도
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    // 사용자가 로그인 취소한 경우에는 추가 로그인 시도하지 않음
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 카카오톡 로그인 실패 시 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공: ${token.accessToken}")
                    performKakaoLogin(token.accessToken)
                    Log.i(TAG, "카카오톡 토큰 : ${token}")
                }
            }
        } else {
            // 카카오톡이 설치되어 있지 않다면 카카오계정으로 로그인 시도
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

    /**
     * 카카오 SDK로 받은 accessToken을 서버에 전달하여 로그인 처리하는 함수
     */
    private fun performKakaoLogin(accessToken: String) {
        lifecycleScope.launch {
            try {
                // GET 방식으로 서버에 로그인 요청: accessToken을 URL 경로의 {code}에 매핑
                val response = RetrofitUtil.userService.kakaoLogin(accessToken)

                if (response.isSuccessful) {
                    // Retrofit의 Response 객체에서 HTTP 응답 헤더를 추출
                    val httpHeaders = response.headers().toMultimap()
                    // "Set-Cookie" 헤더에 들어있는 쿠키 값들을 추출
                    val httpCookies = response.headers().values("Set-Cookie")

                    // HTTP 응답 헤더와 쿠키 로그 출력
                    Log.d(TAG, "HTTP 응답 헤더: $httpHeaders")
                    Log.d(TAG, "HTTP 응답 쿠키: $httpCookies")

                    val sharedPref = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                    val saveToken = sharedPref.getString(KeyAccessToken, "") ?: ""
                    Log.d(TAG, "${httpHeaders["access"]?.get(0)}")
                    val userToken = httpHeaders["access"]?.get(0)
                    SharedPreferencesUtil.saveAccessToken(userToken!!)
                    with(sharedPref.edit()) {
                        putString(KeyAccessToken, userToken)
                        apply()
                    }
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.e(TAG, "서버 카카오 로그인 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "서버 통신 중 에러 발생", e)
            }
        }
    }

}
