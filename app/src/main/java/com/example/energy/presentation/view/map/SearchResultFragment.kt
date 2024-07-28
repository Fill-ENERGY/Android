package com.example.energy.presentation.view.map

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.energy.R
import com.example.energy.databinding.FragmentSearchBinding
import com.example.energy.databinding.FragmentSearchResultBinding
import com.example.energy.presentation.util.MapLocation
import com.example.energy.presentation.view.MainActivity
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.mypage.BlockActivity
import com.example.energy.presentation.viewmodel.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>({ FragmentSearchResultBinding.inflate(it) }) {
    val mapViewModel by activityViewModels<MapViewModel>()
    lateinit var myKakaoMap: KakaoMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView: MapView = binding.mapView

        //상단바
        setToolBar()

        mapViewModel.getStationName.observe(viewLifecycleOwner, Observer { name ->
            binding.tvMarkerBottom.text = name
        })

        //지도 보여주기
        MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) {
                location ->  Log.d("CurrentLocation", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
            getMap(mapView, location)
        }
    }

    private fun getMap(mapView: MapView, location: Location) {
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

                //내 위치
                var labelManager = kakaoMap.labelManager
                if (labelManager != null) {
                    var markerStyle =
                        labelManager.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.iv_marker)))
                    var layer = labelManager.layer
                    if (layer != null) {
                        layer.removeAll()
                        val label =
                            LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                                .setStyles(markerStyle);
                        label.clickable = true
                        layer.addLabel(label)


                    }
                }
            }

            override fun getPosition(): LatLng {
                return LatLng.from(location.latitude, location.longitude)
            }
        })
    }

    private fun setToolBar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu_search_result)
        binding.toolbar.setTitle("서대문구청 전동보장구 급속충전기")
        binding.toolbar.setTitleTextAppearance(requireContext(), R.style.Title2)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.gray_scale8))
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.appbar_search -> {
                    showToast("search")
                    true
                }

                R.id.appbar_sos -> {
                    showToast("sos")
                    true
                }

                else -> false
            }
        }
    }
}