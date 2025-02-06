package com.example.prepay.ui.Login

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.text.Editable
import android.text.Html
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
import androidx.annotation.Dimension.Companion.DP
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentLoginBinding
import com.example.prepay.test_db.UserDBHelper
import com.example.prepay.ui.LoginActivity
import com.example.prepay.ui.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment: BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::bind,
    R.layout.fragment_login
){
    private lateinit var loginActivity: LoginActivity

    private lateinit var DB: UserDBHelper

    //구글 로그인
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var currentuser: FirebaseUser

    private lateinit var dbHelper: UserDBHelper
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

        editTexts = listOf(
            view.findViewById(R.id.login_id_text),
            view.findViewById(R.id.log_in_password_text)
        )


        initEvent()

        setStyleCatchphrase(view)
        initFocusChangeListener()
        setUpTextWatcher()

        // --- login 관련 기능 ---
        dbHelper = UserDBHelper(requireContext())

        binding.LoginBtn.setOnClickListener {

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
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
            signIn()
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
            AbsoluteSizeSpan(21, true),
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
    private fun login() {
        val id = binding.loginIdText.text.toString().trim()
        val password = binding.logInPasswordText.text.toString().trim()

        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (dbHelper.checkUserPassword(id, password)) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
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

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        // Notification Channel ID
    }
}