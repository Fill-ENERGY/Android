package com.example.energy.data.repository.map

import ResultSearchKeyword
import android.util.Log
import com.example.energy.BuildConfig
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.map.search.SearchData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapRepository {
    companion object{
        //리스트 데이터
        fun getListStation(accessToken: String, sort: String, lastId: Int, offset: Int?, currentLatitude: Double, currentLongitude: Double, callback: (List<ListMapModel>?)-> Unit){
            val mapService = getRetrofit().create(MapInterface::class.java)
            val call = mapService.getListStation(accessToken, sort, lastId, offset, currentLatitude, currentLongitude)

            call.enqueue(object : Callback<ListResponse> {
                override fun onResponse(call: Call<ListResponse>, response: Response<ListResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("맵리스트api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val listMapModel = response.body()?.result
                        callback(listMapModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("맵리스트api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ListResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("맵리스트api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        //충전소 개별 조회
        fun getStation(accessToken: String, stationId: Int, currentLatitude: Double, currentLongitude: Double, callback: (StationDetailModel?)-> Unit){
            val mapService = getRetrofit().create(MapInterface::class.java)
            val call = mapService.getStation(accessToken, stationId, currentLatitude, currentLongitude)

            call.enqueue(object : Callback<StationResponse> {
                override fun onResponse(call: Call<StationResponse>, response: Response<StationResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("맵충전소api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val stationDetailModel = response.body()?.result
                        callback(stationDetailModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("맵충전소api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<StationResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("맵충전소api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        //지도에 띄울 데이터
        fun getAllStation(accessToken: String, callback: (List<StationMapModel>?)-> Unit){
            val mapService = getRetrofit().create(MapInterface::class.java)
            val call = mapService.getAllStation(accessToken)

            call.enqueue(object : Callback<MapResponse> {
                override fun onResponse(call: Call<MapResponse>, response: Response<MapResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("맵api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
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