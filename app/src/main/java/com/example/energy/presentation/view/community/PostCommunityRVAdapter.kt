package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.ItemCommunityPostBinding
import com.example.energy.databinding.ItemWritingCommunityImageBinding

class PostCommunityRVAdapter (private val postInfo: ArrayList<CommunityPost>): RecyclerView.Adapter<PostCommunityRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostCommunityRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCommunityPostBinding = ItemCommunityPostBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postInfo[position])
    }


    override fun getItemCount(): Int = postInfo.size

    inner class ViewHolder(val binding: ItemCommunityPostBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(postInfo: CommunityPost) {
            binding.itemCommunityPostUserProfile.setImageResource(postInfo.userProfile)
            binding.itemCommunityPostUserName.text = postInfo.userName
            binding.itemCommunityPostTitle.text = postInfo.title
            binding.itemCommunityPostContent.text = postInfo.content
            binding.itemCommunityPostLikeNum.text = postInfo.likes
            binding.itemCommunityPostCommentNum.text = postInfo.comments
        }
    }
}