package com.example.energy.data.repository.list

import android.util.Log
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.map.ListMapModel
import com.example.energy.data.repository.map.ListResponse
import com.example.energy.data.repository.map.MapInterface
import com.example.energy.data.repository.map.StationDetailModel
import com.example.energy.data.repository.map.StationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRepository {

    companion object{



        //리스트 데이터
        fun getListStation(accessToken: String, sort: String, lastId: Int, offset: Int?, latitude: Double, longitude: Double, callback: (List<ListMapModels>?)-> Unit){
            val listService = getRetrofit().create(ListInterface::class.java)
            val call = listService.getListStation(accessToken, sort, lastId, offset, latitude, longitude)

            call.enqueue(object : Callback<ListResponses> {
                override fun onResponse(call: Call<ListResponses>, response: Response<ListResponses>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("리스트api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val result = response.body()?.result
                        callback(result?.stations)
                    }
                    else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("리스트api테스트", "통신 실패 ${response.code()}, $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ListResponses>, t: Throwable) {
                    // 통신 실패
                    Log.w("리스트api테스트", "통신 실패: ${t.message}")
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
                        Log.d("리스트충전소api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val stationDetailModel = response.body()?.result
                        callback(stationDetailModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("리스트충전소api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<StationResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("리스트충전소api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }


    }
}