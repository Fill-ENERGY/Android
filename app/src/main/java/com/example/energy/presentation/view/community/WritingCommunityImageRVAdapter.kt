package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.databinding.ItemWritingCommunityImageBinding

class WritingCommunityImageRVAdapter (private val imageUrl: ArrayList<WritingCommunityImage>): RecyclerView.Adapter<WritingCommunityImageRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onRemoveImage(position: Int)
    }

    // 외부에서 전달받은 Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): WritingCommunityImageRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemWritingCommunityImageBinding = ItemWritingCommunityImageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WritingCommunityImageRVAdapter.ViewHolder, position: Int) {
        holder.bind(imageUrl[position])

        // X 아이콘 클릭 시 해당데이터 삭제
        holder.binding.writingCommunityImageCancel.setOnClickListener {
            mItemClickListener.onRemoveImage(position)
        }
    }

    override fun getItemCount(): Int = imageUrl.size

    @SuppressLint("NotifyDataSetChanged")
    fun removeImage(position: Int) {
        imageUrl.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ViewHolder(val binding: ItemWritingCommunityImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: WritingCommunityImage) {
            binding.itemWritingCommunityImage.setImageURI(image.imageUrl)
        }
    }

}