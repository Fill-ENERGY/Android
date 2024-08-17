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
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle


class ListInfoFragment : BaseFragment<FragmentListInfoBinding>({ FragmentListInfoBinding.inflate(it) }) {
    //지도 이미지 관련 변수
    lateinit var myKakaoMap: KakaoMap
    lateinit var stationName: String
    var stationLatitude: Double = 0.0
    var stationLongitude: Double = 0.0
    lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달된 데이터를 받아오기
        val stationId = arguments?.getInt("stationId") ?: -1
        val latitude = arguments?.getDouble("latitude") ?: 0.0
        val longitude = arguments?.getDouble("longitude") ?: 0.0

        Log.d("ListInfoFragment", "stationId: $stationId, latitude: $latitude, longitude: $longitude")

        mapView = binding.mapView

        loadStationDetail(stationId, latitude, longitude)

    }

    private fun getMap(
        mapView: MapView,
    ) {
        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                Log.d("카카오맵", "지도 정상 작동")
            }

            override fun onMapError(error: Exception) {
                Log.e("카카오맵", "지도 에러${error}")

            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                myKakaoMap = kakaoMap

                //충전소 위치
                var labelManager = kakaoMap.labelManager
                if (labelManager != null) {
                    var markerStyle =
                        labelManager.addLabelStyles(
                            LabelStyles.from(
                                LabelStyle.from(R.drawable.iv_marker).setTextStyles(
                                    LabelTextStyle.from(32, R.color.gray_scale8)
                                )
                            )
                        )
                    var layer = labelManager.layer
                    if (layer != null) {
                        layer.removeAll()
                        val label =
                            LabelOptions.from(LatLng.from(stationLatitude, stationLongitude))
                                .setStyles(markerStyle)
                                .setTexts(stationName)
                        layer.addLabel(label)
                    }
                }

            }

            override fun getPosition(): LatLng {
                return LatLng.from(stationLatitude, stationLongitude)
            }
        })
    }


    private fun loadStationDetail(stationId: Int, latitude: Double, longitude: Double) {
        //카카오 맵뷰
        val mapView: MapView = binding.mapView

        // API 호출 코드 작성

        if (stationId != -1) {

            var accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODE3OTA5LCJleHAiOjE3MjY0MDk5MDl9.D8cHYgTwnv-k3GdJpSexakAnn7rtZvML1cfkGm9qJoY"


            // 충전소 상세 정보 api 연결
            ListRepository.getStation(accessToken, stationId, latitude, longitude) { stationInfo ->

                if (stationInfo != null) {
                    stationName = stationInfo.name.toString()
                    stationLatitude = stationInfo.latitude!!
                    stationLongitude = stationInfo.longitude!!
                    binding.locationtext.text = stationInfo.address
                    binding.streetnumber.text = stationInfo.streetNumber
                    binding.weekday.text = "${stationInfo.weekdayOpen} ~ ${stationInfo.weekdayClose}"
                    binding.saturday.text = "${stationInfo.saturdayOpen} ~ ${stationInfo.saturdayClose}"
                    binding.holiday.text = "${stationInfo.holidayOpen} ~ ${stationInfo.holidayClose}"
                    binding.calltext.text = stationInfo.phoneNumber
                    binding.currentusagecount.text = stationInfo.concurrentUsageCount.toString()
                    binding.airinjection.text = if (stationInfo.airInjectionAvailable == true) "Y" else "N"

                    //지도 가져오기
                    getMap(mapView)

                } else {
                    Log.e("ListInfoFragment", "데이터 불러오기 실패")
                }
            }
        }
    }
}