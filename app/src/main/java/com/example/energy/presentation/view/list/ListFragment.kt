package com.example.energy.presentation.view.list

import android.os.Bundle
import android.view.View


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.databinding.FragmentListBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListFragment : BaseFragment<FragmentListBinding>({ FragmentListBinding.inflate(it)}) {

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

        val adapter = listAdapter(itemList) { selectedItem ->
            // 클릭된 아이템을 ListInformationFragment로 전달
            val fragment = ListInformationFragment().apply {
                arguments = Bundle().apply {
                    putString("location_name", selectedItem.location_name)
                    putString("distance", selectedItem.distance)
                    putString("grade", selectedItem.grade)
                    putString("time", selectedItem.time)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.list_main, fragment) // fragment_container는 프래그먼트를 담을 컨테이너의 ID
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            //adapter = adapter
            addItemDecoration(CustomDividerItemDecoration(context))
        }
    }
}
