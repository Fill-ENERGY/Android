package com.example.energy.presentation.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.energy.data.model.UserModel
import com.example.energy.data.repository.auth.AuthRepository
import com.example.energy.databinding.ActivityLoginBinding
import com.example.energy.presentation.view.MainActivity
import com.example.energy.presentation.view.base.BaseActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : BaseActivity<ActivityLoginBinding>({ ActivityLoginBinding.inflate(it)}) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kakaoLogin()
        AuthRepository.customLogin {
            response ->
            response.let {
                //통신 성공
                var test = response?.accessToken ?: "dd"

                //토큰 저장
                val sharedPreferences = getSharedPreferences("userToken", MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                Log.d("유저토큰테스트", test)
                editor.putString("accessToken", "Bearer ${response?.accessToken}")
                editor.putString("refreshToken", response?.refreshToken)
                editor.apply()

                //홈으로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        }

    }

    private fun kakaoLogin() {
        binding.btnKakaoLogin.setOnClickListener {
    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("로그인테스트", "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i("로그인테스트", "카카오계정으로 로그인 성공 ${token.accessToken}")
                    loginSuccess(token.accessToken)
                }
            }

    // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("로그인테스트", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("로그인테스트", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        loginSuccess(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun loginFail() {
        showToast("로그인에 실패했습니다.")
    }
    private fun loginSuccess(accessToken: String) {
        showToast("로그인에 성공했습니다.")
        AuthRepository.kakaoLogin(accessToken) {
            response ->
            response.let {
                //통신 성공

                //토큰 저장
                val sharedPreferences = getSharedPreferences("userToken", MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                editor.putString("accessToken", "Bearer ${response?.accessToken}")
                editor.putString("refreshToken", response?.refreshToken)
                editor.apply()

                //홈으로 이동
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

}