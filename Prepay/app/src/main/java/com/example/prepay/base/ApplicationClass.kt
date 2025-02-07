package com.example.prepay

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
    val SERVER_URL = "http://i12d111.p.ssafy.io:8080/"
//    val SERVER_URL = "http://192.168.0.8:9987/"
    //GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정.
    // success, fail등 문자로 리턴될 경우 오류 발생한다. json 문자열이 아니라고..

    override fun onCreate() {
        super.onCreate()

        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)
        // 앱이 처음 생성되는 순간, retrofit 인스턴스를 생성

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
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()

        // 모든 퍼미션 관련 배열
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE

        )
        // 주문 준비 완료 확인 시간 1분
        const val ORDER_COMPLETED_TIME = 60*1000
    }
}