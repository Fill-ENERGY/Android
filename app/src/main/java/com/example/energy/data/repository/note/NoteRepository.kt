package com.example.energy.data.repository.note

import android.util.Log
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.map.StationMapModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoteRepository {
    companion object{


        // 메시지를 보내는 함수
        fun sendMessage(accessToken: String, messageRequest: MessageRequest, callback: (MessageResponse?) -> Unit) {


            val noteApiService = getRetrofit().create(ChatInterface::class.java)
            val call = noteApiService.sendMessages(accessToken, messageRequest)

            call.enqueue(object : Callback<MessageResponse> {

                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    if (response.isSuccessful) {
                        Log.d("NoteRepository", "메시지 전송 성공: ${response.body()?.message}")
                        callback(response.body())
                    } else {
                        Log.e("NoteRepository", "메시지 전송 실패: ${response.errorBody()?.string()}")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e("NoteRepository", "메시지 전송 중 오류 발생: ${t.message}")
                    callback(null)
                }
            })
        }


        fun getChatThreads(accessToken: String, cursor: String, lastId: Int, limit: Int, callback: (List<ChatThread>?) -> Unit)
        {
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.getChatThreads(accessToken, cursor, lastId, limit)

            call.enqueue(object : Callback<ChatThreadsResponse> {

                override fun onResponse(call: Call<ChatThreadsResponse>, response: Response<ChatThreadsResponse>) {
                    if (response.isSuccessful) {

                        val chatThreadsResponse = response.body()?.result?.threads ?: emptyList()
                        callback(chatThreadsResponse)



                    } else {
                        // 서버 응답이 2xx가 아닌 경우
                        val errorBody = response.errorBody()?.string()
                        Log.e("NoteRepository", "응답 실패: ${response.code()} - ${response.message()} \n에러 내용: $errorBody")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ChatThreadsResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("NoteRepository", "채팅방 목록 조회 실패", t)
                    callback(null)
                }
            })
        }

        /*
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



         */



    }

}