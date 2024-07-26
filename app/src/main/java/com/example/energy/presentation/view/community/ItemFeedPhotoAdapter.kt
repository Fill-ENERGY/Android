package com.example.energy.presentation.view.community

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.data.repository.community.WritingCommunityImage
import retrofit2.http.Url

class ItemFeedPhotoAdapter (private val imageUrl: List<Uri>): RecyclerView.Adapter<ItemFeedPhotoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_community_feed_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = imageUrl[position]

        holder.imageView.setImageURI(uri)
    }

    override fun getItemCount(): Int {
        return imageUrl.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_community_feed_photo)
    }
}