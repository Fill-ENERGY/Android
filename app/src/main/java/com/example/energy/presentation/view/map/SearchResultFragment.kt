package com.example.energy.presentation.view.map

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
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
import com.example.energy.presentation.util.EnergyUtils
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
    lateinit var stationName: String
    var stationLatitude: Double = 0.0
    var stationLongitude: Double = 0.0

    lateinit var myKakaoMap: KakaoMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView: MapView = binding.mapView

        //충전소 위도 경도 받아오기
        mapViewModel.getStationLatitude.observe(viewLifecycleOwner, Observer { latitude ->
            stationLatitude = latitude
        })
        mapViewModel.getStationLongitude.observe(viewLifecycleOwner, Observer { longitude ->
            stationLongitude = longitude
        })

        //충전소 정보 세팅
        setStationInfo()

        //지도 보여주기
        MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) {
                location ->  Log.d("CurrentLocation", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
            val distance = mapViewModel.getDistanceFromCurrentLocation(location.latitude, location.longitude)

            //거리 구하기
            binding.tvDistance.text = "${distance} m"

            getMap(mapView, location)
        }
    }

    private fun setStationInfo() {
        mapViewModel.getStationName.observe(viewLifecycleOwner, Observer { name ->
            stationName = name
            //상단바
            setToolBar(name)
            //bottomSheet 타이틀
            binding.tvMarkerBottom.text = name
        })

        //운영시간
        mapViewModel.getStationTime.observe(viewLifecycleOwner, Observer { time ->
            binding.tvTime.text = time
        })

        //충전소 전화번호
        mapViewModel.getStationCall.observe(viewLifecycleOwner, Observer { callNumber ->
            binding.ivCall.setOnClickListener {
                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${callNumber}")

                startActivity(intent)
            }
        })

        //충전소 즐겨찾기
        binding.ivBookmark.setOnClickListener {
            //즐겨찾기 로직 추가

            binding.ivBookmark.setImageResource(R.drawable.iv_bookmark_fill)
        }

        //충전소 길안내
        binding.ivGuide.setOnClickListener {
            //searchCharging(location.latitude, location.longitude, mapViewModel.getLocation())
            mapViewModel.getCurrentLocation.observe(viewLifecycleOwner, Observer { currentLocation ->
             searchCharging(currentLocation, stationLatitude, stationLongitude)
            })
        }

        //충전소 공유
        binding.ivShare.setOnClickListener {
            //기본 셰어 창 연결
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.type = "text/plain"

            val chargingUrl = "https://developer.android.com/training/sharing/send?hl=ko"

            val content = "${stationName}의\n위치를 공유해 보세요!"
            intent.putExtra(Intent.EXTRA_TEXT, "$content\n\n${chargingUrl}")

            val chooserTitle = "친구에게 공유"
            startActivity(Intent.createChooser(intent, chooserTitle))
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

    private fun setToolBar(stationName: String) {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu_search_result)
        binding.toolbar.setTitle(stationName)
        binding.toolbar.setTitleTextAppearance(requireContext(), R.style.Title2)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.gray_scale8))
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.appbar_search -> {
                    showToast("search")
                    true
                }

                R.id.appbar_sos -> {
                    EnergyUtils.showSOSDialog(requireContext())
                    true
                }

                else -> false
            }
        }
    }

    private fun searchCharging(currentLocation: Location, endLatitude: Double, endLongitude: Double) {
        binding.ivGuide.setOnClickListener {
            //카카오 지도로 연결

            val url =
                "kakaomap://route?sp=${currentLocation.latitude},${currentLocation.longitude}&ep=${endLatitude},${endLongitude}&by=FOOT"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)

            // 카카오맵이 설치되어 있다면 앱으로 연결, 설치되어 있지 않다면 스토어로 이동
            if (isAppInstalled("net.daum.android.map", requireContext().packageManager)) {
                //카카오맵으로 이동
                startActivity(intent)
            } else {
                //스토어로 이동
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=net.daum.android.map")
                    )
                )

            }

        }
    }

    private fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }
}