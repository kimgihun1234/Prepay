package com.example.qrscanner.base

import android.app.Application
import retrofit2.Retrofit
import android.Manifest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {


//    val SERVER_URL = "http://192.168.32.98:9987/"
      val SERVER_URL = "https://i12d111.p.ssafy.io/"
//    val SERVER_URL = "http://192.168.0.8:9987/"
    //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정.
    // success, fail등 문자로 리턴될 경우 오류 발생한다. json 문자열이 아니라고..

    override fun onCreate() {
        super.onCreate()


        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS) // 읽기 시간 초과
            .connectTimeout(5000, TimeUnit.MILLISECONDS) // 연결 시간 초과
            .build()


        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient) // OkHttpClient 설정 적용
            .build()
    }

    companion object{
        lateinit var retrofit: Retrofit

        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        // 모든 퍼미션 관련 배열
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
        )
        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60*1000
    }
}