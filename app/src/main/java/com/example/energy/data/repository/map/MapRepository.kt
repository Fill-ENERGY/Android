package com.example.energy.data.repository.map

import ResultSearchKeyword
import android.util.Log
import com.example.energy.BuildConfig
import com.example.energy.data.repository.map.search.SearchData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapRepository {
    companion object{
        //카카오 로그인
        fun getMapModel(accessToken: String, callback: (StationMapModel?)-> Unit){
            val mapService = getRetrofit().create(MapInterface::class.java)
            val call = mapService.getAllStation(accessToken)

            call.enqueue(object : retrofit2.Callback<MapResponse> {
                override fun onResponse(call: Call<MapResponse>, response: Response<MapResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.e("맵api테스트", "통신 성공 ${response.body()?.code}")
                        val mapModel = response.body()?.result
                        callback(mapModel)

                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("맵api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<MapResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("맵api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }
    }
}