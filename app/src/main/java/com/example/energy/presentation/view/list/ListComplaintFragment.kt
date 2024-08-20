package com.example.energy.presentation.view.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.energy.data.repository.list.ListRepository
import com.example.energy.databinding.FragmentListComplaintBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListComplaintFragment : BaseFragment<FragmentListComplaintBinding>({ FragmentListComplaintBinding.inflate(it) }) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 전달된 데이터를 받아오기
        val stationId = arguments?.getInt("stationId") ?: -1
        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0

        Log.d("ListInfoFragment", "stationId: $stationId, latitude: $latitude, longitude: $longitude")




        loadStationDetail(stationId, latitude, longitude)

        //관리기관 정보 복사
        copyToClipboard()

        //민원 글 작성하기 페이지로 이동
        binding.btnWriteComplaint.setOnClickListener {
            //startActivity(Intent(activity, CommunityWritingActivity::class.java))
        }
    }

        //관리 기관 정보 복사
        private fun copyToClipboard() {
        val clipboard: ClipboardManager =
            requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        binding.tvManageCopy.setOnClickListener {
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "label",
                    binding.tvManageInstitution.text.toString()
                )
            )
        }

        binding.tvNumberCopy.setOnClickListener {
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "label",
                    binding.tvNumberInstitution.text.toString()
                )
            )
        }
    }



    private fun loadStationDetail(stationId: Int, latitude: Double, longitude: Double) {
        // API 호출 코드 작성

        if (stationId != -1) {

            var accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODE3OTA5LCJleHAiOjE3MjY0MDk5MDl9.D8cHYgTwnv-k3GdJpSexakAnn7rtZvML1cfkGm9qJoY"


            // 충전소 상세 정보 api 연결
            ListRepository.getStation(accessToken, stationId, latitude, longitude) { stationInfo ->

                if (stationInfo != null) {

                    binding.tvManageInstitution.text = stationInfo.name
                    binding.tvNumberInstitution.text= stationInfo.phoneNumber



                } else {
                    Log.e("ListInfoFragment", "데이터 불러오기 실패")
                }
            }
        }
    }


}