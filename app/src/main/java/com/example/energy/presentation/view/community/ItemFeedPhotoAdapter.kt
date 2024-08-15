package com.example.energy.presentation.view.community

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.data.repository.community.ImagesResponse
import com.example.energy.databinding.ItemCommunityFeedPhotoBinding

class ItemFeedPhotoAdapter (private val imageUrl: List<ImagesResponse>): RecyclerView.Adapter<ItemFeedPhotoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommunityFeedPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val uri = Uri.parse(imageUrl[position]) // String URL을 Uri로 변환
        Glide.with(holder.itemView.context)
            .load(imageUrl[position])
            .into(holder.binding.itemCommunityFeedPhoto)

        Log.d("loadImage", imageUrl[position].toString())
//        val uri = imageUrl[position]
//
//        holder.imageView.setImageURI(uri)
    }

    override fun getItemCount(): Int {
        return imageUrl.size
    }

    inner class ViewHolder(val binding: ItemCommunityFeedPhotoBinding) : RecyclerView.ViewHolder(binding.root)
}