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
        CommunityRepository.getListCommunity(accessToken!!, "", 0, 10, "LATEST") {
                response ->
            response?.let {
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
    }
}