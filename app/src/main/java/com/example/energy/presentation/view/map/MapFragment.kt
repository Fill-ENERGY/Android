package com.example.energy.presentation.view.map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.example.energy.R
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


class MapFragment : BaseFragment<FragmentMapBinding>({ FragmentMapBinding.inflate(it)}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView : MapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {


            override fun onMapDestroy() {
                showToast("지도를 정상적으로 불러왔습니다.")
                Log.d("카카오맵", "지도 정상 작동")
            }

            override fun onMapError(error: Exception) {
                showToast("지도를 불러오지 못했습니다${error}")
                Log.e("카카오맵", "지도 에러${error}")

            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
            }
        })



    }
}