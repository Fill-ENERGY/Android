package com.example.energy.presentation.view.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.databinding.FragmentCommunityScooterBinding
import com.example.energy.presentation.view.base.BaseFragment

class CommunityScooterFragment : BaseFragment<FragmentCommunityScooterBinding>({ FragmentCommunityScooterBinding.inflate(it)}) {

    private lateinit var postCommunityAdapter: PostCommunityRVAdapter
    private var accessToken : String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
        accessToken = sharedPreferences?.getString("accessToken", "none")
    }

    override fun onResume() {
        super.onResume()
        // API를 다시 호출하여 데이터를 갱신
        refreshData()
    }

    private fun refreshData() { // 게시글 조회
        CommunityRepository.getListCommunity(accessToken!!, "SCOOTER", 0, 100, "") {
                response ->
            response.let {
                //통신성공
                if (response != null && response.boards != null) {
                    // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
                    postCommunityAdapter = PostCommunityRVAdapter(response.boards)
                    binding.scooterCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.scooterCommunityPostRv.adapter = postCommunityAdapter
                } else {
                    Log.e("전체커뮤니티api테스트", "응답 결과가 null이거나 board가 없습니다.")
                }
            }
        }
    }
}