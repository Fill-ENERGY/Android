package com.example.energy.data.repository.note

import android.util.Log
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.map.StationMapModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result.response

class NoteRepository {
    companion object{


        // 메시지를 보내는 함수
        fun sendMessage(accessToken: String, messageRequest: MessageRequest?, callback: (MessageResponse?) -> Unit) {


            val noteApiService = getRetrofit().create(ChatInterface::class.java)
            val call = noteApiService.sendMessages(accessToken, messageRequest)

            call.enqueue(object : Callback<MessageResponse> {

                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    if (response.isSuccessful) {
                        Log.d("NoteRepository", "메시지 전송 성공: ${response.body()?.message}")
                        callback(response.body())
                    } else {
                        Log.e("NoteRepository", "메시지 전송 실패: ${response.errorBody()?.string()}!")
                        callback(null)
                    }


                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.e("NoteRepository", "메시지 전송 중 오류 발생: ${t.message}")
                    callback(null)
                }
            })
        }


        //채팅방 전체 목록 조회

        fun getChatThreads(accessToken: String, cursor: String?, lastId: Int?, limit: Int?, callback: (ChatThreadsResponse?) -> Unit)
        {
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.getChatThreads(accessToken, cursor, lastId, limit)

            call.enqueue(object : Callback<ChatThreadsResponse> {

                override fun onResponse(call: Call<ChatThreadsResponse>, response: Response<ChatThreadsResponse>) {
                    if (response.isSuccessful) {
                        Log.d("NoteRepository", "통신 성공 ${response.code()}, ${response.body()?.result}")

                        val chatThreadsResponse = response.body()
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


        //쪽지 목록 조회
        fun getMessages(accessToken: String, threadId: Int?, cursor: Int?, limit: Int?, callback: (GetMessageResponse)-> Unit)
        {
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.getMessages(accessToken, threadId!!, cursor, limit)


            call.enqueue(object : Callback<GetMessageResponse> {

                override fun onResponse(call: Call<GetMessageResponse>, response: Response<GetMessageResponse>) {
                    if (response.isSuccessful) {
                        Log.d("쪽지 목록 조회", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        response.body()?.let { callback(it) }




                    } else {
                        // 서버 응답이 2xx가 아닌 경우
                        val errorBody = response.errorBody()?.string()
                        Log.e("NoteRepository", "응답 실패: ${response.code()} - ${response.message()} \n에러 내용: $errorBody")

                    }
                }

                override fun onFailure(call: Call<GetMessageResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("쪽지목록 조회", "쪽지 목록 조회 실패", t)

                }
            })





    }


        //쪽지 읽음 상태 업데이트
        fun updateReadMessage(accessToken: String, threadId: Int?, cursor: Int?, limit: Int?, callback: (GetMessageResponse)-> Unit) {
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.updateReadMessage(accessToken, threadId!!, cursor, limit)


            call.enqueue(object : Callback<GetMessageResponse> {

                override fun onResponse(
                    call: Call<GetMessageResponse>,
                    response: Response<GetMessageResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("쪽지 읽음 상태 조회", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        response.body()?.let { callback(it) }


                    } else {
                        // 서버 응답이 2xx가 아닌 경우
                        val errorBody = response.errorBody()?.string()
                        Log.e(
                            "쪽지 읽음 상태 조회",
                            "응답 실패: ${response.code()} - ${response.message()} \n에러 내용: $errorBody"
                        )

                    }
                }

                override fun onFailure(call: Call<GetMessageResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("쪽지목록 조회", "쪽지 목록 조회 실패", t)

                }
            })
        }

        //채팅방 나가기

        fun leaveChatRoom(accessToken: String, threadId: Int, callback: (LeaveChatRoomResponse?) -> Unit) {


            val noteApiService = getRetrofit().create(ChatInterface::class.java)
            val call = noteApiService.leaveChatRoom(accessToken, threadId)

            call.enqueue(object : Callback<LeaveChatRoomResponse> {

                override fun onResponse(call: Call<LeaveChatRoomResponse>, response: Response<LeaveChatRoomResponse>) {
                    if (response.isSuccessful) {
                        Log.d("채팅방 나가기", "채팅방 나가기 성공")

                    } else {
                        Log.e("채팅방 나가기", "채팅방 나가기 실패")

                    }
                }

                override fun onFailure(call: Call<LeaveChatRoomResponse>, t: Throwable) {
                    Log.e("채팅방 나가기", "통신 실패")

                }
            })
        }


        // 커뮤니티 -> 채팅방 생성


        fun communityGetMessages(accessToken: String, memberId: Int, callback: (CommunityGetMessagesResponse?) -> Unit)
        {
            val noteService = getRetrofit().create(ChatInterface::class.java)
            val call = noteService.communityGetMessages(accessToken, memberId)


            call.enqueue(object : Callback<CommunityGetMessagesResponse> {

                override fun onResponse(call: Call<CommunityGetMessagesResponse>, response: Response<CommunityGetMessagesResponse>) {
                    if (response.isSuccessful) {

                        Log.d("커뮤니티 채팅방 목록", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        response.body()?.let { callback(it) }


                    } else {
                        // 서버 응답이 2xx가 아닌 경우
                        val errorBody = response.errorBody()?.string()
                        Log.e("커뮤니티 채팅방 목록", "응답 실패: ${response.code()} - ${response.message()} \n에러 내용: $errorBody")

                    }
                }

                override fun onFailure(call: Call<CommunityGetMessagesResponse>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("쪽지목록 조회", "쪽지 목록 조회 실패", t)

                }
            })

        }











    }

}