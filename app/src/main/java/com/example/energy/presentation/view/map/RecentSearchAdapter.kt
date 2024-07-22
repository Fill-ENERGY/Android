package com.example.energy.presentation.view.map

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.map.search.SearchData
import com.example.energy.databinding.ItemRecentBinding
import com.google.gson.Gson

class RecentSearchAdapter( private val context: Context, private val itemList: ArrayList<SearchData>): RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecentSearchAdapter.ViewHolder {
        val binding: ItemRecentBinding = ItemRecentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecentSearchAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position]) //받아온 데이터를 객체에 넣어주는 작업

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(itemList[position])
        }

        holder.binding.ivDelete.setOnClickListener {
            itemClickListener.onRemoveCurrentSearch(position)
        }
    }

    inner class ViewHolder(val binding: ItemRecentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(searchData: SearchData) {
            binding.tvSearchLocation.text = searchData.location

        }
    }

    interface OnItemClickListener {
        //지도로 이동
        fun onItemClick(searchData: SearchData)

        //서치 데이터 지우기
        fun onRemoveCurrentSearch(position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun addItem(searchData: SearchData) {
        if(!itemList.contains(searchData)){
            itemList.add(0, searchData)
            notifyDataSetChanged()
            saveRecentSearches()
        }
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyDataSetChanged()
        saveRecentSearches()
    }

    fun removeAllItem() {
        itemList.clear()
        notifyDataSetChanged()
        saveRecentSearches()
    }

    //최근 검색어 저장
    private fun saveRecentSearches() {
        val sharedPreferences = context.getSharedPreferences("recent_search_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(itemList)
        editor.putString("recent_searches", json)
        editor.apply()
    }
}