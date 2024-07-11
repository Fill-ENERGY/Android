package com.example.energy.presentation.view.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.energy.R
import com.example.energy.databinding.FragmentCommunityBinding
import com.example.energy.presentation.view.base.BaseFragment
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
    }
}