package com.example.energy.presentation.view.community

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CommunityWholeFragment() // 전체보기
            1 -> CommunityDailyFragment() // 일상
            2 -> CommunityCuriousFragment() // 궁금해요
            3 -> CommunityHelpFragment() // 도와줘요
            4 -> CommunityWheelchairFragment() // 휠체어
            else -> CommunityScooterFragment() // 스쿠터
        }
    }
}