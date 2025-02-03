package com.example.prepay.ui.Login

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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

class LoginFragment: BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::bind,
    R.layout.fragment_login
){
    private lateinit var loginActivity: LoginActivity
    private lateinit var DB: UserDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()

        setStyleCatchphrase(view)
        initFocusChangeListener(view)

        // --- login 관련 기능 ---
        DB = UserDBHelper(requireContext())

        binding.LoginBtn.setOnClickListener {
//            login()
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
    private fun initFocusChangeListener(view: View) {
        val editTexts = listOf<EditText>(
            view.findViewById(R.id.sign_in_id_text),
            view.findViewById(R.id.sign_in_password_text)
        )
        editTexts.forEach {
            it.setOnFocusChangeListener { v, isFocus ->
                if (isFocus) {
                    it.setBackgroundResource(R.drawable.focus_shape_alll_round)
                } else {
                    it.setBackgroundResource(R.drawable.shape_all_round)
                }
            }
        }
    }

    // 로그인 함수
    private fun login() {
        val id = binding.signInIdText.text.toString().trim()
        val password = binding.signInPasswordText.text.toString().trim()

        if (id.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (DB.checkUserPassword(id, password)) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

}