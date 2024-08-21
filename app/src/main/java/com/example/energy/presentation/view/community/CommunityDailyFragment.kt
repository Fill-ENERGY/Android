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
    private var accessToken : String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
//        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
//        var accessToken = sharedPreferences?.getString("accessToken", "none")
        accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRqZ3VzaWRAbmF2ZXIuY29tIiwiaWF0IjoxNzI0MTY4NjQwLCJleHAiOjE3MjY3NjA2NDB9.fUaTieyCFhodHH1YTWJTNVTmDFZuvW6RjJ2t_tVzs_M"
    }

    override fun onResume() {
        super.onResume()
        // API를 다시 호출하여 데이터를 갱신
        refreshData()
    }

    private fun refreshData() { // 게시글 조회
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
    }
}