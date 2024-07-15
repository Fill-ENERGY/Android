package com.example.energy.presentation.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.databinding.FragmentCommunityBinding
import com.example.energy.databinding.FragmentListBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListFragment : BaseFragment<FragmentListBinding>({ FragmentListBinding.inflate(it)}) {


    private lateinit var listAdapter: listAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemList = listOf(
            listdata("서대문구청 전동보장구 급속충전기", "16m", "4.41(15)", "(평일) 0:00 ~ 24:00"),
            listdata("연희동 충전소", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            listdata("서대문 보건소", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            listdata("홍은2동주민센터 전동보장구 급속충전기", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            listdata("서울문화체육회관 전동보장구 급속충전기", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            // 더미 데이터 추가
        )

        listAdapter = listAdapter(itemList)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter

            // 구분선 추가
            //val dividerItemDecoration = DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
            //addItemDecoration(dividerItemDecoration)

            // 커스텀 구분선 추가
            addItemDecoration(CustomDividerItemDecoration(context))
        }
    }



}