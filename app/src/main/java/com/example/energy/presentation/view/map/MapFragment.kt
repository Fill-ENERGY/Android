package com.example.energy.presentation.view.map

import android.content.Intent
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

                    }

                    override fun getPosition(): LatLng {
                        return LatLng.from(location.latitude, location.longitude)                }
                })
            }
        }


        //sos 기능
        binding.ivSos.setOnClickListener {
            showSOSDialog()
        }

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
    }
}