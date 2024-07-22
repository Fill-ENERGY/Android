package com.example.energy.presentation.view.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.databinding.FragmentListBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListFragment : BaseFragment<FragmentListBinding>({ FragmentListBinding.inflate(it)}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListFragment", "onViewCreated called")

        val itemList = listOf(
            listdata("서대문구청 전동보장구 급속충전기", "16m", "4.41(15)", "(평일) 0:00 ~ 24:00"),
            listdata("연희동 충전소", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            listdata("서대문 보건소", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            listdata("홍은2동주민센터 전동보장구 급속충전기", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            listdata("서울문화체육회관 전동보장구 급속충전기", "500m", "4.50(20)", "(평일) 8:00 ~ 22:00"),
            // 더미 데이터 추가
        )

        val listadapter = ListAdapter(itemList) { selectedItem ->

            // 클릭된 아이템을 ListInformationActivity로 전달
            val intent = Intent(activity, ListInformationActivity::class.java).apply {
                putExtra("location_name", selectedItem.location_name)
                putExtra("distance", selectedItem.distance)
                putExtra("grade", selectedItem.grade)
                putExtra("time", selectedItem.time)
            }
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listadapter
            addItemDecoration(CustomDividerItemDecoration(context))
        }
    }
}
