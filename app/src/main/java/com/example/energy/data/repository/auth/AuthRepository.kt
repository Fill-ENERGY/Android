package com.example.energy.data.repository.auth

import android.util.Log
import com.example.energy.data.BaseResponse
import com.example.energy.data.getRetrofit
import com.example.energy.data.model.UserModel
import retrofit2.Call
import retrofit2.Response

class AuthRepository {
    companion object{
        //카카오 로그인
        fun kakaoLogin(accessToken: String, callback: (UserModel?)-> Unit){
            val authService = getRetrofit().create(AuthInterface::class.java)
            val call = authService.kakaoLogin("$accessToken")

            call.enqueue(object : retrofit2.Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("카카오api테스트", "통신 성공 ${response.body()?.code}, ${response.body()?.result}")
                        val userModel = response.body()?.result
                        callback(userModel)

                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("카카오api테스트", "통신 실패 ${response.code()}, $error")
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

        //일반 회원가입
        fun customSignUp(callback: ()-> Unit){
            val authService = getRetrofit().create(AuthInterface::class.java)
            val call = authService.customSignUp(AuthSignUpBody())

            call.enqueue(object : retrofit2.Callback<AuthSignUpResponse> {
                override fun onResponse(call: Call<AuthSignUpResponse>, response: Response<AuthSignUpResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("customSignUp", "통신 성공 ${response.body()?.code}")
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("customSignUp", "통신 실패 $error")
                    }
                }

                override fun onFailure(call: Call<AuthSignUpResponse>, t: Throwable) {
                    Log.w("customSignUp", "통신 실패: ${t.message}")
                }
            }
            )
        }

        //로그아웃
        fun logout(accessToken: String, callback: ()-> Unit){
            val authService = getRetrofit().create(AuthInterface::class.java)
            val call = authService.logout(accessToken)

            call.enqueue(object : retrofit2.Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("logout", "통신 성공 ${response.body()?.code}")
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("logout", "통신 실패 $error")
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    Log.w("logout", "통신 실패: ${t.message}")
                }
            }
            )
        }

        //토큰 재발급
        fun refreshToken(accessToken: String, refreshToken: String, callback: ()-> Unit){
            val authService = getRetrofit().create(AuthInterface::class.java)
            val call = authService.refreshToken(accessToken ,refreshToken)

            call.enqueue(object : retrofit2.Callback<RefreshTokenResponse> {
                override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("refreshToken", "통신 성공 ${response.body()}")
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("refreshToken", "통신 실패 $error")
                    }
                }

                override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                    Log.w("logout", "통신 실패: ${t.message}")
                }
            }
            )
        }
    }
}