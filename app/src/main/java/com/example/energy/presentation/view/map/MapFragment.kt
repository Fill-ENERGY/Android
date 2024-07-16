package com.example.energy.presentation.view.map

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.util.MapLocation
import com.example.energy.presentation.view.base.BaseFragment
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions


class MapFragment : BaseFragment<FragmentMapBinding>({ FragmentMapBinding.inflate(it) }) {

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
                getMap(mapView, location)
            }
        }

        //검색 창 넘어가기
        binding.cvSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
            //showBottomSheet()
        }

        //sos 기능
        binding.cvSos.setOnClickListener {
            showSOSDialog()
        }

        //현재 위치 재조정
        binding.ivLocation.setOnClickListener {
            showToast("현재 위치를 가져옵니다")
            MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) {
                    location ->  Log.d("CurrentLocation", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                getMap(mapView, location)
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

                //주소창 텍스트를 현재 주소 기준으로 설정
                binding.tvCurrentLocation.text = MapLocation.getGeoCoder(location.latitude, location.longitude, requireContext())

            }

            override fun getPosition(): LatLng {
                return LatLng.from(location.latitude, location.longitude)
            }
        })
    }


    private fun showSOSDialog() {
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
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

}