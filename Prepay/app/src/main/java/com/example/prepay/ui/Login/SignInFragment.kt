package com.example.prepay.ui.Login

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentSignInBinding
import com.example.prepay.test_db.UserDBHelper
import com.example.prepay.ui.LoginActivity

class SignInFragment: BaseFragment<FragmentSignInBinding>(
    FragmentSignInBinding::bind,
    R.layout.fragment_sign_in
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

        setStyleText(view)


    }

    fun initEvent(){
        binding.signInComplete.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.LOGIN_FRAGMENT)
        }
    }
}

// 문자열 스타일 지정
private fun setStyleText(view: View) {
    val text = "프리페이로 새로운 선결제를 경험해보세요" // 전체 문자열 정의
    val spannableString = SpannableString(text)  // SpannableString 객체 생성

    // "바람" 부분의 시작 인덱스와 끝 인덱스 계산
    val target = "프리페이"
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
        AbsoluteSizeSpan(18, true),
        start,
        end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    // TextView에 설정
    val signInText: TextView = view.findViewById(R.id.sign_in_text)
    signInText.text = spannableString
}