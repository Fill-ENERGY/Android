package com.example.energy.presentation.view.map

import ResultSearchKeyword
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.BuildConfig
import com.example.energy.R
import com.example.energy.data.repository.map.MapInterface
import com.example.energy.data.repository.map.search.SearchData
import com.example.energy.databinding.ActivitySearchBinding
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.DialogSearchDeleteBinding
import com.example.energy.presentation.view.base.BaseActivity
import com.example.energy.presentation.viewmodel.MapViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : BaseActivity<ActivitySearchBinding>({ ActivitySearchBinding.inflate(it)}) {
    private lateinit var mapViewModel: MapViewModel
    private val searchList = ArrayList<SearchData>()
    private val recentSearchList = ArrayList<SearchData>()
    private val searchAdapter = SearchAdapter(searchList)
    private val recentSearchAdapter = RecentSearchAdapter(this, recentSearchList)
    //최근 검색어 로드
    private lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //최근 검색어 불러오기
        sharedPreferences = getSharedPreferences("recent_search_prefs", MODE_PRIVATE)

        //힌트 텍스트 현재 주소로 설정
        binding.etSearch.hint = intent.getStringExtra("hintText")

        //긴급 전화
        binding.cvSos.setOnClickListener {
            showSOSDialog()
        }

        //전체삭제
        binding.tvAllDelete.setOnClickListener {
            showAllDeleteDialog()
        }

        //리사이클러뷰 설정
        setUpRecyclerViews()

        //실시간 검색
        setupSearchEditText()

        //최근 검색어 로드
        loadRecentSearches()
    }

    private fun setUpRecyclerViews() {
        //구분선 적용
        binding.rvList.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        binding.rvRecentList.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }


        binding.rvList.adapter = searchAdapter
        binding.rvRecentList.adapter = recentSearchAdapter

        binding.rvList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchAdapter.setItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(searchData: SearchData) {
                // 아이템을 최근 검색어 리스트에 추가
                recentSearchAdapter.addItem(searchData)
                //데이터 전달
                val bundle = Bundle().apply {
                    putDouble("latitude", searchData.y)
                    putDouble("longitude", searchData.x)
                }

                // MapFragment 인스턴스 생성
                val mapFragment = MapFragment().apply {
                    arguments = bundle
                }

                //search result fragment로 이동
                supportFragmentManager.beginTransaction().replace(R.id.search_activity, SearchResultFragment()).commit()

            }
        }
        )

        binding.rvRecentList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recentSearchAdapter.setItemClickListener(object : RecentSearchAdapter.OnItemClickListener {
            override fun onItemClick(searchData: SearchData) {
                //지도로 이동해서 위치 보여주기
            }

            override fun onRemoveCurrentSearch(position: Int) {
                recentSearchAdapter.removeItem(position)
            }
        })
    }


    //실시간 검색 기능
    private fun setupSearchEditText() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isNotEmpty()) {
                    binding.rvList.visibility = View.VISIBLE
                    binding.rvRecentList.visibility = View.GONE
                    searchKeyword(keyword)
                } else {
                    binding.rvList.visibility = View.GONE
                    binding.rvRecentList.visibility = View.VISIBLE
                }
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

    private fun showSOSDialog() {
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val window = dialog.window
            val layoutParams = window?.attributes

            // 디바이스 너비의 70%로 설정
            val width = (resources.displayMetrics.widthPixels * 0.7).toInt()

            //radius 적용
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            layoutParams?.width = width
            window?.attributes = layoutParams
        }
        dialog.show()

        dialogBinding.btnDialog.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")

            startActivity(intent)
            dialog.dismiss()
        }

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showAllDeleteDialog() {
        val dialogBinding = DialogSearchDeleteBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val window = dialog.window
            val layoutParams = window?.attributes

            // 디바이스 너비의 70%로 설정
            val width = (resources.displayMetrics.widthPixels * 0.7).toInt()

            //radius 적용
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            layoutParams?.width = width
            window?.attributes = layoutParams
        }
        dialog.show()

        //삭제하기
        dialogBinding.btnDelete.setOnClickListener {
            recentSearchAdapter.removeAllItem()
            dialog.dismiss()
        }

        //창 닫기
        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnBack.setOnClickListener {
            dialog.dismiss()
        }
    }

    //최근 검색어 로드
    private fun loadRecentSearches() {
        val gson = Gson()
        val json = sharedPreferences.getString("recent_searches", null)
        val type = object : TypeToken<ArrayList<SearchData>>() {}.type
        if (json != null) {
            recentSearchList.addAll(gson.fromJson(json, type))
            recentSearchAdapter.notifyDataSetChanged()
        }
    }
}