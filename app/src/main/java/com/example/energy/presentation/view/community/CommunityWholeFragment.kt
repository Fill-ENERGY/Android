package com.example.energy.presentation.view.community

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.data.repository.community.WritingCommunityImage
import com.example.energy.databinding.FragmentCommunityWholeBinding
import com.example.energy.presentation.view.base.BaseFragment

class CommunityWholeFragment : BaseFragment<FragmentCommunityWholeBinding>({ FragmentCommunityWholeBinding.inflate(it)}) {

    var postInfo = ArrayList<CommunityPost>() //선택한 이미지 데이터 리스트
    val categoriesList = listOf("도와줘요", "요청 중") //임시 카테고리 리스트
    val imageUrlsList: List<Uri> = emptyList() // 임시 이미지 리스트

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 연결 및 초기화
        binding.wholeCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val postCommunityAdapter = PostCommunityRVAdapter(postInfo)
        binding.wholeCommunityPostRv.adapter = postCommunityAdapter

        // 더미데이터
        postInfo.apply{
            add(CommunityPost(R.drawable.user_profile, "김규리", "연희동 급 SOS", "혹시 지금 연희동 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                categoriesList, imageUrlsList, "1", "3"))
            add(CommunityPost(R.drawable.user_profile, "김규리", "연희동 급 SOS", "혹시 지금 연희동 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                categoriesList, imageUrlsList, "1", "3"))
            add(CommunityPost(R.drawable.user_profile, "김규리", "연희동 급 SOS", "혹시 지금 연희동 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                categoriesList, imageUrlsList, "1", "3"))
            add(CommunityPost(R.drawable.user_profile, "김규리", "연희동 급 SOS", "혹시 지금 연희동 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                categoriesList, imageUrlsList, "1", "3"))
        }
    }
}