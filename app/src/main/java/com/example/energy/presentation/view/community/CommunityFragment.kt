package com.example.energy.presentation.view.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.energy.databinding.FragmentCommunityBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.community.CommunityWritingActivity
import com.google.android.material.tabs.TabLayoutMediator

class CommunityFragment : BaseFragment<FragmentCommunityBinding>({ FragmentCommunityBinding.inflate(it)}) {

    private val information = arrayListOf("전체보기", "일상", "궁금해요", "도와줘요", "휠체어", "스쿠터")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // community viewpager adapter 연결 및 초기화
        val communityAdapter = CommunityVPAdapter(this)
        binding.communityViewpage.adapter = communityAdapter
        TabLayoutMediator(binding.communityCategoryTabLayout, binding.communityViewpage){ // tab item과 viewpage fragment 연결
                tab, position ->
            tab.text = information[position]
        }.attach()

        // 글 쓰기 화면으로 전환
        binding.communityWritingBt.setOnClickListener {
            startActivity(Intent(activity, CommunityWritingActivity::class.java))
        }
    }
}