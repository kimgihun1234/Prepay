package com.example.prepay.ui.Login

import android.graphics.Color
import android.graphics.Rect
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
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import kotlin.math.max
import com.example.prepay.RetrofitUtil
import com.example.prepay.databinding.FragmentSignUpBinding
import com.example.prepay.response.SignupRequest
import com.example.prepay.test_db.UserDBHelper
import com.example.prepay.ui.LoginActivity
import kotlinx.coroutines.launch


class SignupFragment: BaseFragment<FragmentSignUpBinding>(
    FragmentSignUpBinding::bind,
    R.layout.fragment_sign_up
){
    private lateinit var loginActivity: LoginActivity
    private lateinit var editTexts: List<EditText>
    private lateinit var dbHelper: UserDBHelper
    private lateinit var scrollView: ScrollView


    private var checkId = false
    private var checkNickname = false
    private var checkPassword = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        editTexts = listOf(
            view.findViewById(R.id.sign_up_id_text),
            view.findViewById(R.id.sign_up_password_text),
            view.findViewById(R.id.sign_up_password_confirm_text),
            view.findViewById(R.id.sign_up_nick_text)
        )

        scrollView = view.findViewById(R.id.scrollView)

        dbHelper = UserDBHelper(requireContext())

        setStyleText(view)
        initFocusChangeListener()  // editView 테두리 색 바꾸기
        setUpTextWatcher()  // hint 글자 크기 바꾸기
        binding.signUpIdText.doAfterTextChanged {checkUserID()}  // 아이디 유효성 검사
        binding.signUpPasswordText.doAfterTextChanged { checkPassword() }
        binding.signUpPasswordConfirmText.doAfterTextChanged {checkRePassword()} // 비밀번호 재입력 유효성 검사
        binding.signUpNickText.doAfterTextChanged {checkUserNick()}
        binding.signUpSubmit.setOnClickListener { signIn() }

        binding.backBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.START_LOGIN_FRAGMENT)
        }
    }

    // editView focus 이벤트 설정
    private fun initFocusChangeListener() {

        editTexts.forEach {
            it.setOnFocusChangeListener { _, isFocus ->
                if (isFocus) {
                    it.setBackgroundResource(R.drawable.focus_shape_alll_round)
                }
                else {
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
            AbsoluteSizeSpan(20, true),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // TextView에 설정
        val signupText: TextView = view.findViewById(R.id.sign_up_logo_text)
        signupText.text = spannableString
    }


    private fun signIn() {
        val userId = binding.signUpIdText.text.toString().trim()
        val password = binding.signUpPasswordText.text.toString().trim()
        val nickname = binding.signUpNickText.text.toString().trim()

        // 빈 값 확인
        if (userId.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            showToast("정보가 제대로 입력되지 않았습니다.")
            return
        }
        // 유효성 검사 (ID, 비밀번호, 닉네임 모두 통과해야 함)
        if (!checkId || checkPassword || !checkNickname) {
            val insertSuccess = dbHelper.insertData(userId, password, nickname)
            if (insertSuccess) {
                lifecycleScope.launch {
                    try {
                        val signupRequest = SignupRequest(userId, password, nickname)
                        val response = RetrofitUtil.userService.signup(signupRequest)
                        if (response.isSuccessful) {
                            showToast("회원가입에 성공했습니다.")
                            val signupResponse = response.body()
                            if (signupResponse != null && signupResponse.success) {
                                loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.LOGIN_FRAGMENT)
                            } else {
                                // 서버에서 실패 메시지를 전달한 경우
                                showToast(signupResponse?.message ?: "회원가입에 실패했습니다.")
                            }
                        } else {
                            // HTTP 오류 응답 (예: 400, 500 등)
                            showToast("서버 오류: ${response.code()}")
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        showToast("네트워크 오류가 발생했습니다.")
                    }
                }
            } else {
                showToast("입력된 정보를 다시 확인하세요.")
            }
        } else {
            showToast("입력된 정보를 다시 확인하세요.")
        }
    }

    private fun checkUserID() {
        val userId = binding.signUpIdText.text.toString().trim()
        val idPattern = android.util.Patterns.EMAIL_ADDRESS
        val idMessage = view?.findViewById<TextView>(R.id.id_confirm_message)

        if (!userId.matches(idPattern.toRegex())) {
            idMessage?.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
            if (userId.isEmpty()) {
                idMessage?.text = "아이디가 입력되지 않았습니다."
                return
            } else {
                idMessage?.text = "아이디 형식이 올바르지 않습니다."
                return
            }
        }
        checkId = dbHelper.checkId(userId)

        if (!checkId) {
            idMessage?.text  = "사용 가능한 아이디입니다."
            idMessage?.setTextColor(ContextCompat.getColor(requireContext(), R.color.successMessage))
        } else {
            idMessage?.text  = "이미 존재하는 아이디입니다."
            idMessage?.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
        }
    }
    private fun checkPassword() {
        val password = binding.signUpPasswordText.text.toString().trim()
        val passwordMessage = binding.passwordConfirmMessage

        // 비밀번호 길이 확인 (예: 최소 6자 이상)
        if (password.length < 6) {
            passwordMessage.text = "비밀번호는 최소 6자 이상이어야 합니다."
            passwordMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
            checkPassword = false
            return
        } else {
            passwordMessage.text = "사용 가능한 비밀번호입니다."
            passwordMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.successMessage))
        }
    }

    private fun checkRePassword() {
        val password = binding.signUpPasswordText.text.toString().trim()
        val rePassword = binding.signUpPasswordConfirmText.text.toString().trim()
        val rePasswordMessage = binding.rePasswordConfirmMessage // 뷰 바인딩 사용

        // 비밀번호 확인
        if (password == rePassword) {
            rePasswordMessage.text = "비밀번호가 일치합니다."
            rePasswordMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.successMessage))
            checkPassword = true
        } else {
            rePasswordMessage.text = "비밀번호가 일치하지 않습니다."
            rePasswordMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
            checkPassword = false
        }
    }

    private fun checkUserNick() {
        val nickname = binding.signUpNickText.text.toString().trim()
        val nickMessage = binding.signInNickMessage

        // 닉네임 확인
        checkNickname = dbHelper.checkNick(nickname)
        if (!checkNickname) {
            nickMessage.text = "사용가능한 닉네임입니다."
            nickMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.successMessage))
        } else {
            nickMessage.text = "이미 사용 중인 닉네임입니다."
            nickMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
        }
    }
}



