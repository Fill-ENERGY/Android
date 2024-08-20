package com.example.energy.data.repository.review

import android.util.Log
import com.example.energy.data.BaseResponse
import com.example.energy.data.getRetrofit
import com.example.energy.data.model.UserModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response

class ReviewRepository {
    companion object {
        fun postReview(
            accessToken: String,
            content: String,
            score: Double,
            keywords: List<String>?,
            stationId: Int,
            imgUrls: List<String>?
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.postReview(
                accessToken,
                PostReviewBody(content, score, keywords, stationId, imgUrls)
            )

            call.enqueue(object : retrofit2.Callback<PostReviewResponse> {
                override fun onResponse(
                    call: Call<PostReviewResponse>, response: Response<PostReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("postReview", "통신 성공 ${response.body()?.code}")

                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("postReview", "통신 실패 $error")
                    }
                }

                override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("postReview", "통신 실패: ${t.message}")
                }
            }
            )
        }

        fun getReviewInfo(
            accessToken: String,
            reviewId: Int,
            callback: (ReviewModel?) -> Unit
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.getReviewInfo(accessToken, reviewId)

            call.enqueue(object : retrofit2.Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>, response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("getReviewInfo", "통신 성공 ${response.body()?.result}")
                        val reviewModel = response.body()?.result
                        callback(reviewModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("getReviewInfo", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("getReviewInfo", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        fun recommendReview(
            accessToken: String,
            reviewId: Int,
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.recommendReview(accessToken, reviewId)

            call.enqueue(object : retrofit2.Callback<RecommendReviewResponse> {
                override fun onResponse(
                    call: Call<RecommendReviewResponse>, response: Response<RecommendReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("recommendReview", "통신 성공 ${response.body()?.code}")

                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("recommendReview", "통신 실패 $error")
                    }
                }

                override fun onFailure(call: Call<RecommendReviewResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("recommendReview", "통신 실패: ${t.message}")
                }
            }
            )
        }

        fun deleteReview(
            accessToken: String,
            reviewId: Int,
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.deleteReview(accessToken, reviewId)

            call.enqueue(object : retrofit2.Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>, response: Response<BaseResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("deleteReview", "통신 성공 ${response.body()?.code}")

                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("deleteReview", "통신 실패 $error")
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("deleteReview", "통신 실패: ${t.message}")
                }
            }
            )
        }

        fun editReview(
            accessToken: String,
            reviewId: Int,
            content: String,
            score: Double,
            keywords: List<String>?,
            imgUrls: List<String>?,
            callback: (ReviewModel?) -> Unit
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.editReview(
                accessToken,
                reviewId,
                PatchReviewBody(content, score, keywords, imgUrls)
            )

            call.enqueue(object : retrofit2.Callback<ReviewResponse> {
                override fun onResponse(
                    call: Call<ReviewResponse>, response: Response<ReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("editReview", "통신 성공 ${response.body()?.code}")
                        val reviewEditModel = response.body()?.result
                        callback(reviewEditModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("editReview", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("editReview", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        fun postImages(
            accessToken: String,
            imgUrls: List<MultipartBody.Part>?,
            callback: (ImageModel?) -> Unit
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.postImages(accessToken, imgUrls)

            call.enqueue(object : retrofit2.Callback<UploadImageResponse> {
                override fun onResponse(
                    call: Call<UploadImageResponse>, response: Response<UploadImageResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("postImages", "통신 성공 ${response.body()?.code}")
                        val imageModel = response.body()?.result
                        callback(imageModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("postImages", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("postImages", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

//        fun getMyReviews(
//            accessToken: String,
//            callback: (List<ReviewModel>?) -> Unit
//        ) {
//            val reviewService = getRetrofit().create(ReviewInterface::class.java)
//            val call = reviewService.getMyReviews(accessToken)
//
//            call.enqueue(object : retrofit2.Callback<AllReviewResponse> {
//                override fun onResponse(
//                    call: Call<AllReviewResponse>, response: Response<AllReviewResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        //통신 성공
//                        Log.d("getMyReviews", "통신 성공 ${response.body()?.result}")
//                        val reviewModel = response.body()?.result
//                        callback(reviewModel)
//                    } else {
//                        //통신 실패
//                        val error = response.errorBody()?.toString()
//                        Log.e("getMyReviews", "통신 실패 $error")
//                        callback(null)
//                    }
//                }
//
//                override fun onFailure(call: Call<AllReviewResponse>, t: Throwable) {
//                    // 통신 실패
//                    Log.w("getMyReviews", "통신 실패: ${t.message}")
//                    callback(null)
//                }
//            }
//            )
//        }

        fun getReviewsStation(
            accessToken: String,
            stationId: Int,
            lastId: Int,
            query: String,
            offset: Int,
            callback: (List<ReviewModel>?) -> Unit
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call =
                reviewService.getReviewsStation(accessToken, stationId, lastId, query, offset)

            call.enqueue(object : retrofit2.Callback<AllReviewResponse> {
                override fun onResponse(
                    call: Call<AllReviewResponse>, response: Response<AllReviewResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("getReviewsStation", "통신 성공 ${response.body()?.result}")
                        val reviewModel = response.body()?.result?.reviews
                        callback(reviewModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("getReviewsStation", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<AllReviewResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("getReviewsStation", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        fun getReviewKeywords(
            accessToken: String,
            callback: (List<KeywordModel>?) -> Unit
        ) {
            val reviewService = getRetrofit().create(ReviewInterface::class.java)
            val call = reviewService.getReviewKeywords(accessToken)

            call.enqueue(object : retrofit2.Callback<AllKeywordResponse> {
                override fun onResponse(
                    call: Call<AllKeywordResponse>, response: Response<AllKeywordResponse>
                ) {
                    if (response.isSuccessful) {
                        //통신 성공
                        Log.d("getReviewKeywords", "통신 성공 ${response.body()?.result}")
                        val keywordModel = response.body()?.result
                        callback(keywordModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.toString()
                        Log.e("getReviewKeywords", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<AllKeywordResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("getReviewKeywords", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }



    }
}