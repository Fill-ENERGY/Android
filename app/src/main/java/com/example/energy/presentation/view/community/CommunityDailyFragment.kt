package com.example.energy.presentation.view.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.databinding.FragmentCommunityDailyBinding
import com.example.energy.presentation.view.base.BaseFragment

class CommunityDailyFragment : BaseFragment<FragmentCommunityDailyBinding>({ FragmentCommunityDailyBinding.inflate(it)}) {

    private lateinit var postCommunityAdapter: PostCommunityRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
//        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
//        var accessToken = sharedPreferences?.getString("accessToken", "none")
        val accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODg3ODYzLCJleHAiOjE3MjY0Nzk4NjN9.qGR9PibGimGon0_82i_Z73nxXJzK1BDoPLWRLjC0QI4"

        //test
        CommunityRepository.getListCommunity(accessToken!!, "DAILY", 0, 10, "") {
                response ->
            response.let {
                //통신성공
                if (response != null && response.boards != null) {
                    // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
                    postCommunityAdapter = PostCommunityRVAdapter(response.boards)
                    binding.dailyCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.dailyCommunityPostRv.adapter = postCommunityAdapter
                } else {
                    Log.e("전체커뮤니티api테스트", "응답 결과가 null이거나 board가 없습니다.")
                }
            }
        }

//        // community_post에 데이터 리스트 생성
//        communityDB = CommunityPostDatabase.getInstance(requireContext())!!
//
//        // 백그라운드 스레드에서 데이터베이스 접근
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                val allPosts = communityDB.communityPostDao().getAllPosts()
//                postInfo.addAll(allPosts.filter { it.categoryString == "일상" })
//            }
//            // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
//            postCommunityAdapter = PostCommunityRVAdapter(postInfo)
//            binding.dailyCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            binding.dailyCommunityPostRv.adapter = postCommunityAdapter
//        }
    }
}