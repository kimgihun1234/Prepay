package com.example.prepay

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class LocaleInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val locale = Locale.getDefault().toString() // e.g., "en_US"
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept-Language", locale) // 헤더에 locale 추가
            .build()
        return chain.proceed(request)
    }
}
