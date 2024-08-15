package com.example.energy.data.repository.community

import android.util.Log
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.map.ListMapModel
import com.example.energy.data.repository.map.ListResponse
import com.example.energy.data.repository.map.MapInterface
import com.example.energy.data.repository.map.MapResponse
import com.example.energy.data.repository.map.StationDetailModel
import com.example.energy.data.repository.map.StationMapModel
import com.example.energy.data.repository.map.StationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityRepository {
    companion object{

        // 전체 커뮤니티 조회
        fun getListCommunity(accessToken: String, category: String, cursor: Long, limit: Int, sort: String, callback: (CommunityModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.getListCommunity(accessToken, category, cursor, limit, sort)

            call.enqueue(object : Callback<CommunityResponse> {
                override fun onResponse(call: Call<CommunityResponse>, response: Response<CommunityResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("전체커뮤니티api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val communityModel = response.body()?.result
                        callback(communityModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("전체커뮤니티api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CommunityResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("전체커뮤니티api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            })
        }

        // 커뮤니티 상세 조회
        fun getDetailCommunity(accessToken: String, boardId: Long, callback: (DetailModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.getDetailCommunity(accessToken, boardId)

            call.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("개별커뮤니티api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val communityDetailModel = response.body()?.result
                        callback(communityDetailModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("개별커뮤니티api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("개별커뮤니티api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        // 커뮤니티 글 작성
        fun postBoard(accessToken: String, request: PostBoardRequest, callback: (BoardModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.postBoard(accessToken, request)

            call.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티작성api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postBoardModel = response.body()?.result
                        callback(postBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티작성api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티작성api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        // 커뮤니티 글 좋아요 추가
        fun postLikeBoard(accessToken: String, boardId: Long, callback: (LikeModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.postLikeBoard(accessToken, boardId)

            call.enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티좋아요추가api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postLikeBoardModel = response.body()?.result
                        callback(postLikeBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티좋아요추가api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티좋아요추가api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }
    }
}