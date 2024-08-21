package com.example.energy.presentation.view.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energy.R
import com.example.energy.data.repository.list.ListRepository
import com.example.energy.databinding.ActivityListInformationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ListInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListInformationBinding



    //탭 레이아웃 항목

    private val tabTitleArray = arrayOf(
        "정보",
        "평가",
        "민원"
    )


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityListInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)




        //토큰 가져오기
        var sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = sharedPreferences?.getString("accessToken", "none")



        val stationId = intent.getIntExtra("stationId", -1)
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)


        if (stationId != -1) {


            //상세 정보 api 호출
            if (accessToken != null) {
                ListRepository.getStation(accessToken, stationId, latitude, longitude) { stationInfo ->

                    if (stationInfo != null) {
                        binding.LocationName.text = stationInfo.name
                        binding.Distance.text = stationInfo.distance

                        val formattedscore = String.format("%.2f", stationInfo.score)
                        binding.Grade.text = "$formattedscore(${stationInfo.scoreCount})"


                    } else {
                        Log.e("ListInformationActivity", "상세 정보 api 연결 실패")
                    }
                }
            }
        }




        // 어댑터 초기화
        val adapter = ListVPAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        // 어댑터 데이터 업데이트
        adapter.updateData(stationId, latitude, longitude)



        //뒤로 가기 버튼 클릭 시
        binding.backbutton.setOnClickListener {
            finish()
        }




    }





}