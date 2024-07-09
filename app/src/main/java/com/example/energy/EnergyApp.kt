package com.example.energy

import android.app.Application
import com.example.energy.BuildConfig.KAKAO_NATIVE_KEY
import com.kakao.sdk.common.KakaoSdk

class EnergyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, KAKAO_NATIVE_KEY)
    }
}