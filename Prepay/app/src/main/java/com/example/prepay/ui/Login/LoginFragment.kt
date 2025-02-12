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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentLoginBinding
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
        val sharedPref = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)

        // 저장된 아이디와 비밀번호 읽어오기 (저장되지 않았다면 기본값은 빈 문자열로)
//        val savedId = sharedPref.getString(keyUserId, "") ?: ""
//        val savedPw = sharedPref.getString(keyUserPw, "") ?: ""

//        // 저장된 아이디와 패스워드가 모두 있으면, 자동으로 로그인 API를 호출하여 자동 로그인 처리
//        if (savedId.isNotEmpty() && savedPw.isNotEmpty()) {
//            // 자동 로그인: 저장된 값으로 login() 함수를 호출
//            login(savedId, savedPw)
//            return
//        }

        // 카카오 로그인 버튼 클릭 처리 (예: testButton)
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }


        editTexts = listOf(
            view.findViewById(R.id.login_id_text),
            view.findViewById(R.id.log_in_password_text)
        )

        initEvent()

        setStyleCatchphrase(view)
        initFocusChangeListener()
        setUpTextWatcher()

        // --- login 관련 기능 ---

        binding.LoginBtn.setOnClickListener {
            login()
        }


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
                }
            })
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
            try {
//                val loginRequest = LoginRequest(userId, userPw)

                // 사용자가 '자동 로그인' 체크한 경우, 입력한 아이디/비밀번호를 저장
                if (binding.autoIdCheckbox.isChecked) {
                    val sharedPref =
                        requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString(keyUserId, userId)
                        putString(keyUserPw, userPw)
                        apply()
                    }
                }
                Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()

            } catch (e: Exception) {
                e.printStackTrace()
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
        // 로그인 조합 예제

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

}