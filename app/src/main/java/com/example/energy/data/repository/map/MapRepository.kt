package com.example.energy.data.repository.map

import android.util.Log
import com.example.energy.data.BaseResponse
import com.example.energy.data.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapRepository {
    companion object{
       //충전소 즐겨찾기
       fun postBookmarkStation(accessToken: String, stationId: Any){
           val mapService = getRetrofit().create(MapInterface::class.java)
           val call = mapService.postBookmarkStation(accessToken, stationId)

           call.enqueue(object : Callback<BaseResponse> {
               override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>
               ) {
                   if(response.isSuccessful){
                       //통신 성공
                       Log.d("postBookmarkStation", "통신 성공 ${response.code()}, ${response.body()?.result}")
                   } else {
                       //통신 실패
                       val error = response.errorBody()?.toString()
                       Log.e("postBookmarkStation", "통신 실패 $error")
                   }
               }

               override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                   // 통신 실패
                   Log.w("postBookmarkStation", "통신 실패: ${t.message}")
               }
           }
           )
       }


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
        fun getStation(accessToken: String, stationId: Any, currentLatitude: Double, currentLongitude: Double, callback: (StationDetailModel?)-> Unit){
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

        //즐겨찾기한 충전소 조회
        fun getBookmarkStation(accessToken: String, query: String, lastId: Int, offset: Int?, currentLatitude: Double, currentLongitude: Double, callback: (List<StationBookmarkModel>?)-> Unit){
            val mapService = getRetrofit().create(MapInterface::class.java)
            val call = mapService.getBookmarkStation(accessToken, query, lastId, offset, currentLatitude, currentLongitude)

            call.enqueue(object : Callback<StationBookmarkResponse> {
                override fun onResponse(call: Call<StationBookmarkResponse>, response: Response<StationBookmarkResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("getBookmarkStation", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val mapModel = response.body()?.result?.stations
                        callback(mapModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("getBookmarkStation", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<StationBookmarkResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("getBookmarkStation", "통신 실패: ${t.message}")
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