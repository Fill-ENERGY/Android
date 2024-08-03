package com.example.energy.presentation.view.community

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.R
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.data.repository.community.WritingCommunityImage
import com.example.energy.databinding.ItemCommunityFeedPhotoBinding
import retrofit2.http.Url

class ItemFeedPhotoAdapter (private val imageUrl: List<Uri>): RecyclerView.Adapter<ItemFeedPhotoAdapter.ViewHolder>(){

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