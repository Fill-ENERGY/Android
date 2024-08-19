package com.example.energy.presentation.view.community

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.data.repository.community.BoardModel
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.databinding.FragmentCommunityWholeBinding
import com.example.energy.presentation.view.base.BaseFragment

class CommunityWholeFragment : BaseFragment<FragmentCommunityWholeBinding>({ FragmentCommunityWholeBinding.inflate(it)}) {

    val categoriesList = "도와줘요" //임시 카테고리 리스트
    val imageUrlsList: List<Uri> = emptyList() // 임시 이미지 리스트
    private lateinit var postCommunityAdapter: PostCommunityRVAdapter
    private val postList = mutableListOf<BoardModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
//        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
//        var accessToken = sharedPreferences?.getString("accessToken", "none")
        val accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzOTg1OTUxLCJleHAiOjE3MjY1Nzc5NTF9.jEn8OyBau-JQ576OLgESOD0dGcGH614WfsQUGGbtq_M"

        // 게시글 조회 test
        CommunityRepository.getListCommunity(accessToken, "", 0, 10, "LATEST") {
                response ->
            response.let {
                Log.d("게시글정보", "${response}")
                //통신성공
                if (response != null && response.boards != null) {
                    // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
                    postCommunityAdapter = PostCommunityRVAdapter(response.boards)
                    binding.wholeCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.wholeCommunityPostRv.adapter = postCommunityAdapter
                } else {
                    Log.e("전체커뮤니티api테스트", "응답 결과가 null이거나 board가 없습니다. ${response}")
                }
            }
        }

//        // community_post에 데이터 리스트 생성
//        communityDB = CommunityPostDatabase.getInstance(requireContext())!!
//
//        // 백그라운드 스레드에서 데이터베이스 접근
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                postInfo.addAll(communityDB.communityPostDao().getAllPosts())
//            }
//            // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
//            postCommunityAdapter = PostCommunityRVAdapter(postInfo)
//            binding.wholeCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            binding.wholeCommunityPostRv.adapter = postCommunityAdapter
//        }
    }

//    // 게시글 리스트 업데이트 메서드
//    fun updatePostList(newPost: CommunityPost) {
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                postInfo.add(newPost)
//            }
//            postCommunityAdapter.notifyItemInserted(postInfo.size + 1)
//            binding.wholeCommunityPostRv.scrollToPosition(postInfo.size + 1)
//        }
//    }
}