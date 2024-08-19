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
        fun getListCommunity(accessToken: String, category: String, cursor: Int, limit: Int, sort: String, callback: (CommunityModel?)-> Unit){
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
        fun getDetailCommunity(accessToken: String, boardId: Int, callback: (BoardModel?)-> Unit){
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


        // 커뮤니티 글 수정
        fun updateBoard(accessToken: String, boardId: Int, request: PostBoardRequest, callback: (BoardModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.updateBoard(accessToken, boardId, request)

            call.enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티수정api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postBoardModel = response.body()?.result
                        callback(postBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티수정api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티수정api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }


        // 커뮤니티 글 삭제
        fun deleteBoard(accessToken: String, boardId: Int, callback: (DeleteCommunityResponse?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.deleteBoard(accessToken, boardId)

            call.enqueue(object : Callback<DeleteCommunityResponse> {
                override fun onResponse(call: Call<DeleteCommunityResponse>, response: Response<DeleteCommunityResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티삭제api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postBoardModel = response.body()
                        callback(postBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티삭제api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<DeleteCommunityResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티삭제api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        // 이미지 업로드
        fun uploadImages(accessToken: String, request: UploadImagesRequest, callback: (ImagesModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.uploadImages(accessToken, request)

            call.enqueue(object : Callback<UploadImagesResponse> {
                override fun onResponse(call: Call<UploadImagesResponse>, response: Response<UploadImagesResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("이미지업로드api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val imagesModel = response.body()?.result
                        callback(imagesModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("이미지업로드api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<UploadImagesResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("이미지업로드api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        // 커뮤니티 글 좋아요 추가
        fun postLikeBoard(accessToken: String, boardId: Int, callback: (LikeModel?)-> Unit){
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
        // 커뮤니티 글 좋아요 삭제
        fun deleteLikeBoard(accessToken: String, boardId: Int, callback: (LikeModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.deleteLikeBoard(accessToken, boardId)

            call.enqueue(object : Callback<LikeResponse> {
                override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티좋아요삭제api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postLikeBoardModel = response.body()?.result
                        callback(postLikeBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티좋아요삭제api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티좋아요삭제api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }


        // 커뮤니티 댓글 조회
        fun getListComment(accessToken: String, boardId: Int, callback: (List<CommentModel>?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.getListComment(accessToken, boardId)

            call.enqueue(object : Callback<CommentListResponse> {
                override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티댓글조회api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val commentModel = response.body()?.result!!.comments
                        callback(commentModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티댓글조회api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티댓글조회api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            })
        }

        // 커뮤니티 댓글 작성
        fun writeCommentBoard(accessToken: String, boardId: Int, request: WriteCommentRequest, callback: (CommentModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.writeCommentBoard(accessToken, boardId, request)

            call.enqueue(object : Callback<CommentResponse> {
                override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티댓글작성api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val writeCommentBoardModel = response.body()?.result
                        callback(writeCommentBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티댓글작성api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티댓글작성api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }


        // 커뮤니티 댓글 수정
        fun updateComment(accessToken: String, boardId: Int, commentId: Int, request: UpdateCommentRequest, callback: (CommentModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.updateComment(accessToken, boardId, commentId, request)

            call.enqueue(object : Callback<CommentResponse> {
                override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("댓글수정api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postBoardModel = response.body()?.result
                        callback(postBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("댓글수정api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("댓글수정api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }


        // 커뮤니티 댓글 삭제
        fun deleteComment(accessToken: String, boardId: Int, commentId: Int, callback: (DeleteCommentResponse?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.deleteComment(accessToken, boardId, commentId)

            call.enqueue(object : Callback<DeleteCommentResponse> {
                override fun onResponse(call: Call<DeleteCommentResponse>, response: Response<DeleteCommentResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("커뮤니티댓글삭제api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val postBoardModel = response.body()
                        callback(postBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("커뮤니티댓글삭제api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<DeleteCommentResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("커뮤니티댓글삭제api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }



        // 도와줘요 상태 변경
        fun helpStatus(accessToken: String, boardId: Int, request: HelpStatusRequest, callback: (HelpStatusModel?)-> Unit){
            val communityService = getRetrofit().create(CommunityInterface::class.java)
            val call = communityService.helpStatus(accessToken, boardId, request)

            call.enqueue(object : Callback<HelpStatusResponse> {
                override fun onResponse(call: Call<HelpStatusResponse>, response: Response<HelpStatusResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("도와줘요상태변경api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                        val writeCommentBoardModel = response.body()?.result
                        callback(writeCommentBoardModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("도와줘요상태변경api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<HelpStatusResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("도와줘요상태변경api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }
    }
}