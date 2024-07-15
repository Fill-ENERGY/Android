package com.example.energy.presentation.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.databinding.ListItemBinding


class listAdapter(private val itemList: List<listdata>) : RecyclerView.Adapter<listAdapter.ListViewHolder>() {

    // ViewHolder 정의
    class ListViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: listdata) {
            binding.locationName.text = data.location_name
            binding.distance.text = data.distance
            binding.grade.text = data.grade
            binding.time.text = data.time
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
        holder.bind(itemList[position])
    }

    // 항목의 개수를 반환
    override fun getItemCount(): Int = itemList.size


}