package com.example.energy.presentation.view.list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R


import com.example.energy.data.repository.list.ListRepository

import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.FragmentListBinding
import com.example.energy.presentation.util.EnergyUtils.Companion.showSOSDialog
import com.example.energy.presentation.util.MapLocation
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.viewmodel.MapViewModel

class ListFragment : BaseFragment<FragmentListBinding>({ FragmentListBinding.inflate(it)}) {

    //sort 초기값 거리순으로 설정
    private var currentSortType = "DISTANCE"

    // MapViewModel 주입
    private val mapViewModel: MapViewModel by activityViewModels()


    // 구분선 초기 설정
    private var isDecorationAdded = false

    var currentLatitude: Double = 0.0
    var currentLongitude: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Log.d("ListFragment", "onViewCreated called")

        //토큰 가져오기
        //var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)

        var accessToken ="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InduZGtkdXMxMDJAbmF2ZXIuY29tIiwiaWF0IjoxNzI0MTY0NTU2LCJleHAiOjE3MjY3NTY1NTZ9.xRtumUjlAyuRhf7Ldu_7kH52XBFzqdaP6nTy0OjfvuQ"


        //데이터 로드 함수 호출
        loadData(accessToken, currentSortType)
        MapLocation.getCurrentLocation(requireContext(), this, requireActivity()) {
            location -> Log.d("CurrentLocation", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
            currentLatitude = location.latitude
            currentLongitude = location.longitude

            mapViewModel.setCurrentLocation(location)

        }




        // 별점순 버튼 클릭 시
        binding.star.setOnClickListener {

            // 정렬 기준 별점순으로 변경
            currentSortType="SCORE"
            loadData(accessToken, currentSortType)


            // 색상 변경
            binding.star.setTextColor(Color.parseColor("#222019"))
            binding.meter.setTextColor(Color.parseColor("#71716E"))

        }


        //거리순 버튼 클릭 시
        binding.meter.setOnClickListener {

            currentSortType="DISTANCE"
            loadData(accessToken, currentSortType)

            // 색상 변경
            binding.meter.setTextColor(Color.parseColor("#222019"))
            binding.star.setTextColor(Color.parseColor("#71716E"))
        }






        //sos 기능
        binding.cvSos.setOnClickListener {
            showSOSDialog()
        }








    }


    private fun loadData(accessToken: String?, sortType: String) {


        ListRepository.getListStation(accessToken!!, sortType, 0, 100, currentLatitude, currentLongitude)

        { result ->

            if (result != null) {

                Log.d("ListFragment", "데이터를 성공적으로 가져왔습니다: $result")


                // 리스트 어댑터 생성
                val listAdapter = ListAdapter(result, mapViewModel) { selectedItem ->

                    // 클릭된 아이템을 ListInformationActivity로 전달
                    val intent = Intent(activity, ListInformationActivity::class.java).apply {

                        putExtra("stationId", selectedItem.id)
                        putExtra("latitude", currentLatitude)
                        putExtra("longitude", currentLongitude)
                        //putExtra("grade", "${selectedItem.score.toString()}(${selectedItem.scoreCount})")
                        //putExtra("time", "${selectedItem.openTime} ~ ${selectedItem.closeTime}")

                    }
                    startActivity(intent)


                }

                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = listAdapter

                    // 구분선이 아직 추가되지 않은 경우에만 추가

                    if (!isDecorationAdded) {
                        addItemDecoration(CustomDividerItemDecoration(context))
                        isDecorationAdded = true
                    }

                }


            } else {
                Log.e("ListFragment", "Failed to fetch data")
            }
        }
    }




    private fun showSOSDialog() {
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val window = dialog.window
            val layoutParams = window?.attributes

            // 디바이스 너비의 70%로 설정
            val width = (resources.displayMetrics.widthPixels * 0.7).toInt()

            //radius 적용
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            layoutParams?.width = width
            window?.attributes = layoutParams
        }
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
