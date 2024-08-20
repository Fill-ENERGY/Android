package com.example.energy.data.repository.profile

import android.util.Log
import com.example.energy.data.getRetrofit
import retrofit2.Call
import retrofit2.Response

class ProfileRepository {
    companion object {
        fun getMyProfile(
            accessToken: String,
            callback: (MyPageProfileModel?) -> Unit
        ) {
            val profileService = getRetrofit().create(ProfileInterface::class.java)
            val call = profileService.getMyProfile(accessToken)

            call.enqueue(object : retrofit2.Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>, response: Response<ProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("getMyProfile", "통신 성공 ${response.body()?.result}")
                        val reviewModel = response.body()?.result
                        callback(reviewModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("getMyProfile", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("getMyProfile", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }
    }
}
