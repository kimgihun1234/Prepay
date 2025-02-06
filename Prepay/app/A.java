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