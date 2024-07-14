package com.example.energy.presentation.view.map

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.map.SearchData
import com.example.energy.databinding.ActivitySearchBinding
import com.example.energy.presentation.view.base.BaseActivity

class SearchActivity : BaseActivity<ActivitySearchBinding>({ ActivitySearchBinding.inflate(it)}) {
    private var searchDummy = ArrayList<SearchData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 더미 데이터 추가
        searchDummy.apply {
            add(SearchData("자연사박물관"))
            add(SearchData("연사박물관"))
            add(SearchData("박물과앙"))
        }

        val searchAdapter = SearchAdapter(searchDummy)
        //구분선 적용
        binding.rvList.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        binding.rvList.adapter = searchAdapter
        binding.rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchAdapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(searchData: SearchData) {
                showToast("click text")
            }

            override fun onRemoveCurrentSearch(position: Int) {
                searchAdapter.removeItem(position)
            }
        })
    }

}