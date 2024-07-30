package com.example.energy.presentation.view.map

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.energy.R
import com.example.energy.data.model.MarkerModel
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.DialogLoginBinding
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.util.EnergyUtils
import com.example.energy.presentation.util.MapLocation
import com.example.energy.presentation.view.MainActivity
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.login.LoginActivity
import com.example.energy.presentation.viewmodel.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle
import com.kakao.vectormap.shape.MapPoints


class MapFragment : BaseFragment<FragmentMapBinding>({ FragmentMapBinding.inflate(it) }) {
    val mapViewModel by activityViewModels<MapViewModel>()
    lateinit var myKakaoMap: KakaoMap
    private var seachLatitude: Double = 0.0
    private var searchLongitude: Double = 0.0

    val markerList = ArrayList<MarkerModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView: MapView = binding.mapView

        if (!MapLocation.hasPermission(requireContext())) {
            requestPermissions(
                MapLocation.MAPPERMISSIONS,
                MapLocation.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) {
                location ->  Log.d("CurrentLocation", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")

                //뷰모델에 현재 위치 전달
                mapViewModel.setCurrentLocation(location)

                getMap(mapView, location)
            }
        }

        //검색 창 넘어가기
        binding.cvSearch.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra("hintText", binding.tvCurrentLocation.text)
            startActivity(intent)
        }

        //sos 기능
        binding.cvSos.setOnClickListener {
            EnergyUtils.showSOSDialog(requireContext())
        }

        //현재 위치 재조정
        binding.ivLocation.setOnClickListener {
            showToast("현재 위치를 가져옵니다")
            MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) {
                    location ->  Log.d("CurrentLocation", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")

                //뷰모델에 현재 위치 전달
                mapViewModel.setCurrentLocation(location)

                getMap(mapView, location)
            }

            //bundle로 데이터 받기
            arguments?.let { bundle ->
                seachLatitude = bundle.getDouble("latitude", 0.0)
                searchLongitude = bundle.getDouble("longitude", 0.0)
                Log.d("검색데이터전달", "${searchLongitude}, ${seachLatitude}")
            }
        }


    }

    //bottomsheet 펼침
    private fun showBottomSheet() {
        val bottomSheetDialogFragment = MapInfoBottomSheet()
        val bundle = Bundle()
        // 현재 위치 데이터를 Bundle에 넣어 전달
        MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) { location ->
            bundle.putParcelable("location", location)
            bottomSheetDialogFragment.arguments = bundle
            bottomSheetDialogFragment.show(
                requireActivity().supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
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

                //주소창 텍스트를 현재 주소 기준으로 설정
                binding.tvCurrentLocation.text = MapLocation.getGeoCoder(location.latitude, location.longitude, requireContext())

                //setMarker(kakaoMap, markerList)

                // 현재 위치를 표시하거나 초기 위치를 설정하는 로직
                if (seachLatitude != 0.0 && searchLongitude != 0.0) {
                    //지도에 선택한 위치 표시하는 로직
                    //myKakaoMap.setCenter(location.latitude, location.longitude)
                }

                //내 위치
                var labelManager = kakaoMap.labelManager
                if (labelManager != null) {
                    var markerStyle =
                        labelManager.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.iv_marker).setTextStyles(
                            LabelTextStyle.from(32, R.color.gray_scale8))))
                    var layer = labelManager.layer
                    if (layer != null) {
                        layer.removeAll()
                            val label =
                                LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                                    .setStyles(markerStyle)
                                    .setTexts("")
                            label.clickable = true
                            layer.addLabel(label)


                    }
                }

                //마커 클릭 이벤트
                myKakaoMap.setOnLabelClickListener { kakaoMap, layer, label ->
                    //showBottomSheet()

                    mapViewModel.setStationName("my town")
                    mapViewModel.setStationLongitude(location.longitude)
                    mapViewModel.setStationLatitude(location.latitude)
                    mapViewModel.setStationTime("정상영업")
                    mapViewModel.setStationCall("010")

                    //마커 클릭 시 충전소 이름 갱신
                    //label.changeText("my town")
                    (context as MainActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchResultFragment())
                        .commitAllowingStateLoss()
                }
            }

            override fun getPosition(): LatLng {
                return LatLng.from(location.latitude, location.longitude)
            }
        })
    }

    //마커 띄우기
    private fun setMarker(kakaoMap: KakaoMap, markerList: ArrayList<MarkerModel>) {
        var labelManager = kakaoMap.labelManager
        if (labelManager != null) {
            var markerStyle =
                labelManager.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.iv_marker)))
            var layer = labelManager.layer
            if (layer != null) {
                layer.removeAll()
                for (data in markerList) {
                    val label =
                        LabelOptions.from(LatLng.from(data.latitude, data.longitude))
                            .setStyles(markerStyle);
                    label.clickable = true
                    layer.addLabel(label)

                }
            }
        }
    }
    
}