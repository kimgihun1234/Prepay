package com.example.prepay

import android.content.Context
import android.content.SharedPreferences
import com.example.prepay.data.response.TeamUserRes

private const val TAG = "SharedPreferencesUtil_싸피"

class SharedPreferencesUtil(context: Context) {
    val SHARED_PREFERENCES_NAME = "smartstore_preference"
    val COOKIES_KEY_NAME = "cookies"

    var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    //사용자 정보 저장
    /*
    fun addUser(teamUserRes: TeamUserRes){
        val editor = preferences.edit()
        editor.putString("id", teamUserRes.id)
        editor.putString("name", teamUserRes.name)
        editor.apply()
    }

    fun getUser(): TeamUserRes {
        val id = preferences.getString("id", "")
        if (id != "") {
            val name = preferences.getString("name", "")
            return TeamUserRes(id!!, name!!, "")
        } else {
            return TeamUserRes()
        }
    }*/

    fun deleteUser(){
        //preference 지우기
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    fun addUserCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(COOKIES_KEY_NAME, cookies)
        editor.apply()
    }

    fun getUserCookie(): MutableSet<String>? {
        return preferences.getStringSet(COOKIES_KEY_NAME, HashSet())
    }

    fun deleteUserCookie() {
        preferences.edit().remove(COOKIES_KEY_NAME).apply()
    }
}
