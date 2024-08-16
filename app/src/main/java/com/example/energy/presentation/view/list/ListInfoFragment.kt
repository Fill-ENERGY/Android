package com.example.energy.presentation.view.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.energy.R
import com.example.energy.data.repository.list.ListRepository
import com.example.energy.databinding.FragmentListComplaintBinding
import com.example.energy.databinding.FragmentListInfoBinding
import com.example.energy.presentation.view.base.BaseFragment


class ListInfoFragment : BaseFragment<FragmentListInfoBinding>({ FragmentListInfoBinding.inflate(it) }) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 전달된 데이터를 받아오기
        val stationId = arguments?.getInt("stationId") ?: -1
        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0

        Log.d("ListInfoFragment", "stationId: $stationId, latitude: $latitude, longitude: $longitude")


        loadStationDetail(stationId, latitude, longitude)


    }


    private fun loadStationDetail(stationId: Int, latitude: Double, longitude: Double) {
        // API 호출 코드 작성

        if (stationId != -1) {

            var accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODE3OTA5LCJleHAiOjE3MjY0MDk5MDl9.D8cHYgTwnv-k3GdJpSexakAnn7rtZvML1cfkGm9qJoY"


            // 충전소 상세 정보 api 연결
            ListRepository.getStation(accessToken, stationId, latitude, longitude) { stationInfo ->

                if (stationInfo != null) {

                    binding.locationtext.text = stationInfo.address
                    binding.streetnumber.text = stationInfo.streetNumber
                    binding.weekday.text = "${stationInfo.weekdayOpen} ~ ${stationInfo.weekdayClose}"
                    binding.saturday.text = "${stationInfo.saturdayOpen} ~ ${stationInfo.saturdayClose}"
                    binding.holiday.text = "${stationInfo.holidayOpen} ~ ${stationInfo.holidayClose}"
                    binding.calltext.text = stationInfo.phoneNumber
                    binding.currentusagecount.text = stationInfo.concurrentUsageCount.toString()
                    binding.airinjection.text = if (stationInfo.airInjectionAvailable == true) "Y" else "N"



                } else {
                    Log.e("ListInfoFragment", "데이터 불러오기 실패")
                }
            }
        }
    }


}