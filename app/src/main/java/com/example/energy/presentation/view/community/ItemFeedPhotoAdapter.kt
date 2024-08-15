package com.example.energy.presentation.view.community

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.data.repository.community.ImagesModel
import com.example.energy.databinding.ItemCommunityFeedPhotoBinding

class ItemFeedPhotoAdapter (private val imageUrl: List<ImagesModel>): RecyclerView.Adapter<ItemFeedPhotoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommunityFeedPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(imageUrl[position].img_uri)
            .into(holder.binding.itemCommunityFeedPhoto)

        Log.d("loadImage", imageUrl[position].img_uri)
//        val uri = imageUrl[position]
//
//        holder.imageView.setImageURI(uri)
    }

    override fun getItemCount(): Int {
        return imageUrl.size
    }

    inner class ViewHolder(val binding: ItemCommunityFeedPhotoBinding) : RecyclerView.ViewHolder(binding.root)
}