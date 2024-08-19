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


        fun fetchChatThreads(accessToken: String, lastId: Int, limit: Int, callback: (ChatThreadsResponse?) -> Unit)
        {
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.getChatThreads(accessToken)

            call.enqueue(object : Callback<ChatThreadsResponse> {
                override fun onResponse(call: Call<ChatThreadsResponse>, response: Response<ChatThreadsResponse>) {
                    if (response.isSuccessful) {
                        val chatThreads = response.body()?.result?.threads ?: emptyList()


                        val targetThread = chatThreads.find { it.receiverId.toInt() == 1 }

                        if (targetThread != null) {
                            val threadId = targetThread.threadId
                            val receiverId = targetThread.receiverId
                            // 쪽지 전송 시 이 값들을 사용합니다.
                        }
                    } else {
                        // 오류 처리
                    }
                }

                override fun onFailure(call: Call<ChatThreadsResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("NoteRepository", "채팅방 목록 조회 실패")

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