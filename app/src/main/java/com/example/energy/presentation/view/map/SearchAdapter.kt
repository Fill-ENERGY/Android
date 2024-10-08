package com.example.energy.presentation.view.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.map.search.SearchData
import com.example.energy.databinding.ItemSearchBinding

class SearchAdapter(private val itemList: ArrayList<SearchData>): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding: ItemSearchBinding = ItemSearchBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position]) //받아온 데이터를 객체에 넣어주는 작업

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(itemList[position])
        }

    }

    inner class ViewHolder(val binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(searchData: SearchData) {
            binding.tvSearchLocation.text = searchData.location

        }
    }

    interface OnItemClickListener {
        //지도로 이동 및 최근 검색어 추가
        fun onItemClick(searchData: SearchData)

    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun addItem(searchData: SearchData) {
        itemList.add(searchData)
        notifyDataSetChanged()
    }

}