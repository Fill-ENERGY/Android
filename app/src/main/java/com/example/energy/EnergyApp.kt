package com.example.energy

import android.app.Application
import android.util.Log
import com.example.energy.BuildConfig.KAKAO_NATIVE_KEY
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class EnergyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao 키해시 확인
        val keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", "$keyHash")

        // Kakao SDK 초기화
        KakaoSdk.init(this, KAKAO_NATIVE_KEY)
    }
}