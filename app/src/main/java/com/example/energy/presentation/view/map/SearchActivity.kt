package com.example.energy.presentation.view.map

import ResultSearchKeyword
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.BuildConfig
import com.example.energy.R
import com.example.energy.data.repository.map.MapInterface
import com.example.energy.data.repository.map.SearchData
import com.example.energy.databinding.ActivitySearchBinding
import com.example.energy.presentation.util.MapLocation
import com.example.energy.presentation.view.base.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : BaseActivity<ActivitySearchBinding>({ ActivitySearchBinding.inflate(it)}) {
    private val searchList = ArrayList<SearchData>()
    private val searchAdapter = SearchAdapter(searchList)
    private var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.cvSos.setOnClickListener {
            keyword = binding.etSearch.text.toString()
            Log.d("키워드 입력 테스트", keyword)
            searchKeyword(keyword)
        }


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

    private fun addSearchResult(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
    // 검색 결과 있음
            searchList.clear() // 리스트 초기화
            for (document in searchResult!!.documents) {
    // 결과를 리사이클러 뷰에 추가
                val item = SearchData(
                    document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x.toDouble(),
                    document.y.toDouble()
                )
                searchList.add(item)

            }

            searchAdapter.notifyDataSetChanged()


        } else {
    // 검색 결과 없음
            showToast("검색 결과가 없습니다")
        }
    }

    fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(MapInterface::class.java) // 통신 인터페이스를 객체로 생성
        val call = api.getSearchKeyword("KakaoAK ${BuildConfig.KAKAO_REST_KEY}", keyword) // 검색 조건 입력

        // API 서버에 요청
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                Log.d("검색테스트", "Raw: ${response.raw()}")
                Log.d("검색테스트", "Body: ${response.body()}")

                //리사이클러뷰에 결과 추가
                addSearchResult(response.body())
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                // 통신 실패
                Log.w("검색테스트", "통신 실패: ${t.message}")
            }
        }
        )
    }

}