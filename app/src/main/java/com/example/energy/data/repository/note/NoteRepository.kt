package com.example.energy.data.repository.note

import android.util.Log
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.map.StationMapModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteRepository {
    companion object{
        //지도에 띄울 데이터
        fun leaveChatRoom(accessToken: String, threadId: Int, callback: (StationMapModel?)-> Unit){
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.leaveChatRoom(accessToken, threadId)

            call.enqueue(object : Callback<LeaveChatResponse> {
                override fun onResponse(call: Call<LeaveChatResponse>, response: Response<LeaveChatResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.e("맵api테스트", "통신 성공 ${response.body()?.code}")


                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("맵api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<LeaveChatResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("맵api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }
    }

}