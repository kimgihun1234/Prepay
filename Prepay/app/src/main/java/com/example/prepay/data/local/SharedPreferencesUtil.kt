package com.example.prepay

import android.content.Context
import android.content.SharedPreferences
import com.example.prepay.data.response.TeamUserRes

private const val TAG = "SharedPreferencesUtil_싸피"

object SharedPreferencesUtil {
    private const val SHARED_PREFERENCES_NAME = "prepay_preference"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_PW = "user_pw"
    private const val KEY_ACCESS_TOKEN = "access_token"

    private lateinit var preferences: SharedPreferences

    // 초기화 메서드
    fun init(context: Context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    // 사용자 자격 증명 저장
    fun saveUserCredentials(id: String, password: String) {
        preferences.edit().apply {
            putString(KEY_USER_ID, id)
            putString(KEY_USER_PW, password)
            apply()
        }
    }

    // 사용자 자격 증명 불러오기
    fun getUserCredentials(): Pair<String?, String?> {
        val id = preferences.getString(KEY_USER_ID, null)
        val password = preferences.getString(KEY_USER_PW, null)
        return Pair(id, password)
    }

    // 액세스 토큰 저장
    fun saveAccessToken(accessToken: String) {
        preferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            apply()
        }
    }

    // 액세스 토큰 불러오기
    fun getAccessToken(): String? {
        return preferences.getString(KEY_ACCESS_TOKEN, null)
    }

    // 모든 사용자 데이터 삭제
    fun clearUserData() {
        preferences.edit().clear().apply()
    }
}
