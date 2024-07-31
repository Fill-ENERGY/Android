package com.example.energy.data.repository.auth

import android.util.Log
import com.example.energy.data.getRetrofit
import com.example.energy.data.model.UserModel
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AuthRepository {
    companion object{
        //카카오 로그인
        fun kakaoLogin(accessToken: String, callback: (UserModel?)-> Unit){
            val authService = getRetrofit().create(AuthInterface::class.java)
            val call = authService.kakaoLogin(accessToken)

            call.enqueue(object : retrofit2.Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.e("카카오api테스트", "통신 성공 ${response.body()?.code}")
                        val userModel = response.body()?.result
                        callback(userModel)

                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("카카오api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("카카오api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        //회원탈퇴
    }
}