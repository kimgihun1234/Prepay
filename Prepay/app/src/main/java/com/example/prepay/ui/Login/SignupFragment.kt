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

        initFocusChangeListener()  // editView 테두리 색 바꾸기

        binding.signUpIdText.doAfterTextChanged {checkUserID()}  // 아이디 유효성 검사
        binding.signUpPasswordText.doAfterTextChanged { checkPassword() }
        binding.signUpPasswordConfirmText.doAfterTextChanged {checkRePassword()} // 비밀번호 재입력 유효성 검사
        binding.signUpNickText.doAfterTextChanged {checkUserNick()}
        binding.signUpSubmit.setOnClickListener { signUp() }
        setUpTextWatcher()  // hint 글자 크기 바꾸기
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
    // EditView 크기 변환 이벤트
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
        // 이메일 사용 가능(!checkId), 비밀번호 일치(checkPassword), 닉네임 사용 가능(!checkNickname)여야 함.
        if (checkId && checkPassword && checkNickname) {
            binding.signUpSubmit.setBackgroundResource(R.drawable.submit_button_style)
        } else {
            binding.signUpSubmit.setBackgroundResource(R.drawable.disable_button_style)
        }
        Log.d("ssafy", "${checkId}, ${checkPassword}, $checkNickname" )
    }

    // 회원가입 코드
    private fun signUp() {
        val userId = binding.signUpIdText.text.toString().trim()
        val password = binding.signUpPasswordText.text.toString().trim()
        val nickname = binding.signUpNickText.text.toString().trim()

        // 빈 값 확인
        if (userId.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
            showToast("정보가 제대로 입력되지 않았습니다.")
            return
        }
        // 유효성 검사 (ID, 비밀번호, 닉네임 모두 통과해야 함)
        if (checkId || checkPassword || checkNickname) {
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
    // 아이디 체크
    private fun checkUserID() {
        val userId = binding.signUpIdText.text.toString().trim()
        val idPattern = android.util.Patterns.EMAIL_ADDRESS
        val idMessage = view?.findViewById<TextView>(R.id.id_confirm_message)

        if (!userId.matches(idPattern.toRegex())) {
            idMessage?.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
            if (userId.isEmpty()) {
                idMessage?.text = "이메일이 입력되지 않았습니다."
                return
            } else {
                idMessage?.text = "이메일 형식이 올바르지 않습니다."
                return
            }
        }
        checkId = !dbHelper.checkId(userId)

        if (checkId) {
            idMessage?.text  = "사용 가능한 이메일입니다."
            idMessage?.setTextColor(ContextCompat.getColor(requireContext(), R.color.successMessage))
        } else {
            idMessage?.text  = "사용 중인 이메일입니다."
            idMessage?.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
        }
    }
    private fun checkPassword() {
        val password = binding.signUpPasswordText.text.toString().trim()
        val passwordMessage = binding.passwordConfirmMessage

        // 비밀번호 길이 확인 (예: 최소 6자 이상)
        if (password.isEmpty()) {
            passwordMessage.text = "비밀번호가 입력되지 않았습니다."
            passwordMessage.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorMessage))
        }
        else {
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
    }

    private fun checkRePassword() {
        val password = binding.signUpPasswordText.text.toString().trim()
        val rePassword = binding.signUpPasswordConfirmText.text.toString().trim()
        val rePasswordMessage = binding.rePasswordConfirmMessage // 뷰 바인딩 사용

        // 비밀번호 확인
        if (password.isNotEmpty() && password == rePassword) {
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
        if (nickname.isEmpty()) {
            nickMessage.text = "닉네임이 입력되지 않았습니다."
            checkNickname = false
            nickMessage.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.errorMessage
                )
            )
        } else {
            checkNickname = dbHelper.checkNick(nickname)
            if (!checkNickname) {
                nickMessage.text = "사용가능한 닉네임입니다."
                checkNickname = true
                nickMessage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.successMessage
                    )
                )
            } else {
                nickMessage.text = "이미 사용 중인 닉네임입니다."
                nickMessage.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.errorMessage
                    )
                )
                checkNickname = false
            }
        }
    }
}



