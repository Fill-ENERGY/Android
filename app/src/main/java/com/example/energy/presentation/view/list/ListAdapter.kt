package com.example.energy.presentation.view.list

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.list.ListMapModels
import com.example.energy.data.repository.map.ListMapModel
import com.example.energy.databinding.ListItemBinding
import com.example.energy.presentation.viewmodel.MapViewModel


class ListAdapter(private val itemList: List<ListMapModels>,
                  private val mapViewModel: MapViewModel,
                  private val onItemClick: (ListMapModels) -> Unit
    ) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {










    // ViewHolder 정의
    class ListViewHolder(private val binding: ListItemBinding,
                         private val mapViewModel: MapViewModel)
        : RecyclerView.ViewHolder(binding.root) {





        fun bind(data: ListMapModels, onItemClick: (ListMapModels) -> Unit) {

            //거리, 평점, 시간 출력
            binding.locationName.text = data.name
            binding.distance.text = data.distance
            binding.grade.text = "${data.score.toString()}(${data.scoreCount})"
            binding.time.text = "${data.openTime} ~ ${data.closeTime}"


            itemView.setOnClickListener { onItemClick(data) } // 클릭 리스너 설정



            // 전화 버튼 클릭 시 다이얼로 연결

            binding.callButton.setOnClickListener {

                var phoneNumber = data.institutionPhone

                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")

                itemView.context.startActivity(intent)

            }


            binding.naviButton.setOnClickListener {

                Log.d("ListAdapter", "naviButton 클릭됨")

                // Fragment의 ViewLifecycleOwner를 올바르게 가져옵니다.
                val fragment = (binding.root.context as? androidx.fragment.app.Fragment)
                val viewLifecycleOwner = fragment?.viewLifecycleOwner

                if (viewLifecycleOwner != null) {
                    mapViewModel.getCurrentLocation.observe(viewLifecycleOwner, Observer { currentLocation ->
                        Log.d("ListAdapter", "현재 위치: $currentLocation")
                        // 목적지의 위도와 경도를 사용해서 길안내
                        data.latitude?.let { it1 ->
                            data.longitude?.let { it2 ->
                                searchCharging(currentLocation, it1, it2)
                            }
                        }
                    })
                } else {
                    Log.e("ListAdapter", "ViewLifecycleOwner를 가져오지 못했습니다.")
                }
            }




        }

        private fun searchCharging(currentLocation: Location, endLatitude: Double, endLongitude: Double) {


            //카카오 지도로 연결

            val url = "kakaomap://route?sp=${currentLocation.latitude},${currentLocation.longitude}&ep=${endLatitude},${endLongitude}&by=FOOT"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)

            // 카카오맵이 설치되어 있다면 앱으로 연결, 설치되어 있지 않다면 스토어로 이동
            if (isAppInstalled("net.daum.android.map", binding.root.context.packageManager)) {
                //카카오맵으로 이동
                binding.root.context.startActivity(intent)
            } else {
                binding.root.context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map"))
                )
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

    // RecyclerView의 각 항목을 위한 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding, mapViewModel)

    }

    // ViewHolder에 데이터를 바인딩
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.d("ListAdapter", "Binding item at position $position")
        holder.bind(itemList?.get(position) ?: return, onItemClick)


    }



    // 항목의 개수를 반환
    override fun getItemCount(): Int = itemList.size





}