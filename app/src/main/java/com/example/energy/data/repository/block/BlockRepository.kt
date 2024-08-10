package com.example.energy.data.repository.block

import android.util.Log
import com.example.energy.data.BaseResponse
import com.example.energy.data.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockRepository {
    companion object{
        //차단하기
        fun postBlockMember(accessToken: String, targetMemberId: Int, callback: ()-> Unit){
            val blockService = getRetrofit().create(BlockInterface::class.java)
            val call = blockService.postBlockMember(accessToken, targetMemberId)

            call.enqueue(object : Callback<BlockResponse> {
                override fun onResponse(call: Call<BlockResponse>, response: Response<BlockResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("차단api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.string()
                        Log.e("차단api테스트", "통신 실패 $error")

                        //"code":"BLOCK403_2","message":"자기 자신은 차단할 수 없습니다."
                        //"code":"BLOCK400","message":"이미 차단된 사용자입니다."}
                    }
                }

                override fun onFailure(call: Call<BlockResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("차단api테스트", "통신 실패: ${t.message}")
                }
            }
            )
        }

        //차단 멤버 전체 조회
        fun getBlockMembers(accessToken: String, cursor: Int, limit: Int, callback: (BlockUserModel?)-> Unit){
            val blockService = getRetrofit().create(BlockInterface::class.java)
            val call = blockService.getBlockMembers(accessToken, cursor, limit)

            call.enqueue(object : Callback<BlockListResponse> {
                override fun onResponse(call: Call<BlockListResponse>, response: Response<BlockListResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("차단조회api테스트", "통신 성공 ${response.code()}, ${response.body()}")
                        val blockUsersModel = response.body()?.result?.blocks
                        //callback(blockUsersModel)
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.string()
                        Log.e("차단조회api테스트", "통신 실패 $error")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<BlockListResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("차단조회api테스트", "통신 실패: ${t.message}")
                    callback(null)
                }
            }
            )
        }

        //차단 해제
        fun deleteBlockMember(accessToken: String, blockId: Int, callback: ()-> Unit){
            val blockService = getRetrofit().create(BlockInterface::class.java)
            val call = blockService.deleteBlockMember(accessToken, blockId)

            call.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>
                ) {
                    if(response.isSuccessful){
                        //통신 성공
                        Log.d("차단해제api테스트", "통신 성공 ${response.code()}, ${response.body()?.result}")
                    } else {
                        //통신 실패
                        val error = response.errorBody()?.string()
                        Log.e("차단해제api테스트", "통신 실패 $error")

                        //"code":"BLOCK404","message":"차단 내역을 찾을 수 없습니다."
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    // 통신 실패
                    Log.w("차단해제api테스트", "통신 실패: ${t.message}")
                }
            }
            )
        }
    }
}