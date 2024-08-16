package com.example.energy.presentation.view.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.map.ListMapModel
import com.example.energy.databinding.ListItemBinding


class ListAdapter(private val itemList: List<ListMapModel>,
                  private val onItemClick: (ListMapModel) -> Unit
    ) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {





    // ViewHolder 정의
    class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(data: ListMapModel, onItemClick: (ListMapModel) -> Unit) {

            //거리, 평점, 시간 출력
            binding.locationName.text = data.name
            binding.distance.text = data.distance
            binding.grade.text = "${data.score.toString()}(${data.scoreCount})"
            binding.time.text = "${data.openTime} ~ ${data.closeTime}"


            itemView.setOnClickListener { onItemClick(data) } // 클릭 리스너 설정



        }
    }

    // RecyclerView의 각 항목을 위한 ViewHolder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    // ViewHolder에 데이터를 바인딩
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Log.d("ListAdapter", "Binding item at position $position")
        holder.bind(itemList?.get(position) ?: return, onItemClick)


    }



    // 항목의 개수를 반환
    override fun getItemCount(): Int = itemList.size


}