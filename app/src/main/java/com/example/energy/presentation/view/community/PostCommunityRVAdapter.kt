package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.ItemCommunityFeedBinding


class PostCommunityRVAdapter (private val postInfo: ArrayList<CommunityPost>): RecyclerView.Adapter<PostCommunityRVAdapter.ViewHolder>() {

    interface PeopleItemClickListener {
        fun onItemClick(position: Int, community: CommunityPost) // 각 인원수 아이템 클릭 시 반응하는 함수
        fun onCheckIconClick(community: CommunityPost) //체크 이미지 클릭 시 반응하는 함수
    }

    // 외부에서 전달받은 Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: PeopleItemClickListener
    fun setMyItemClickListener(itemClickListener: PeopleItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun clickItem(position: Int) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostCommunityRVAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemCommunityFeedBinding = ItemCommunityFeedBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postInfo[position])
    }


    override fun getItemCount(): Int = postInfo.size

    inner class ViewHolder(val binding: ItemCommunityFeedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(postInfo: CommunityPost) {
            binding.itemCommunityPostUserProfile.setImageResource(postInfo.userProfile)
            binding.itemCommunityPostUserName.text = postInfo.userName
            binding.itemCommunityPostTitle.text = postInfo.title
            binding.itemCommunityPostContent.text = postInfo.content
            binding.itemCommunityPostLikeNum.text = postInfo.likes
            binding.itemCommunityPostCommentNum.text = postInfo.comments

            // 아이템 제목 및 내용 클릭 시 상세 페이지로 이동
            binding.itemCommunityPostContainer.setOnClickListener {
                val intent = Intent(binding.root.context, CommunityDetailActivity::class.java)
                intent.putExtra("postInfo", postInfo)
                binding.root.context.startActivity(intent)
            }
            // 댓글 쓰기 클릭 시 상세 페이지로 이동
            binding.itemCommunityPostCommentTv.setOnClickListener {
                val intent = Intent(binding.root.context, CommunityDetailActivity::class.java)
                intent.putExtra("postInfo", postInfo)
                binding.root.context.startActivity(intent)
            }
        }


    }
}