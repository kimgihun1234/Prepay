package com.example.prepay

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object CommonUtils {

    //천단위 콤마
    fun makeComma(num: Int): String {
        val comma = DecimalFormat("#,###")
        return "${comma.format(num)} ₩"
    }

    //날짜 포맷 출력
    fun dateformatYMDHM(time:Date):String{
        val format = SimpleDateFormat("yyyy.MM.dd. HH:mm", Locale.KOREA)
        format.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        return format.format(time)
    }

    fun dateformatYMD(time: Date):String{
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        format.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        return format.format(time)
    }

    fun formatLongToDate(longDate: Long): String {
        val format = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())  // 원하는 날짜 형식 지정
        return format.format(Date(longDate))  // Long 값을 Date 객체로 변환 후 포맷 적용
    }

    enum class LoginFragmentName(var str: String) {
        LOGIN_FRAGMENT("LoginFragment"),
        SIGNIN_FRAGMENT("SignInFragment"),
    }

    enum class MainFragmentName(var str: String) {
        MYPAGE_FRAGMENT("MyPageFragment"),
        GROUP_SEARCH_FRAGMENT("GroupSearchFragment"),
        CREATE_PRIVATE_GROUP_FRAGMENT("CreatePrivateGroupFragment"),
        CREATE_PUBLIC_GROUP_FRAGMENT("CreatePublicGroupFragment"),
        GROUP_DETAILS_FRAGMENT("GroupDetailsFragment"),
        LOOK_GROUP_FRAGMENT("LookGroupFragment"),
        RESTAURANT_DETAILS_FRAGMENT("RestaurantDetailsFragment"),
    }

}