package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.R
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.BoardModel
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.ItemCommunityFeedBinding


class PostCommunityRVAdapter (private var postInfo: List<BoardModel>): RecyclerView.Adapter<PostCommunityRVAdapter.ViewHolder>() {

    interface PeopleItemClickListener {
        fun onItemClick(position: Int, community: CommunityPost)
        fun onCheckIconClick(community: CommunityPost)
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

        // 카테고리 String -> Int로 바꾸는 함수
        fun fromString(category: String): Int {
            return when (category) {
                "DAILY" -> R.drawable.tag_daily
                "INQUIRY" -> R.drawable.tag_curious
                "HELP" -> R.drawable.tag_help
                "WHEELCHAIR" -> R.drawable.tag_wheelchair
                else -> R.drawable.tag_scooter
            }
        }

        fun bind(postInfo: BoardModel) {
//            binding.itemCommunityPostUserProfile.setImageResource(postInfo.userProfile!!)
            binding.itemCommunityPostUserName.text = postInfo.member_name
            binding.itemCommunityPostTitle.text = postInfo.title
            binding.itemCommunityPostContent.text = postInfo.content
            binding.itemCommunityPostLikeNum.text = postInfo.like_num.toString()
            binding.itemCommunityPostCommentNum.text = postInfo.comment_count.toString()
            binding.itemCommunityPostCategoryView.setImageResource(fromString(postInfo.category!!))

//            // 좋아요 아이콘 설정
//            select(postInfo.isLiked)

            // 이미지 RecyclerView 설정
            if(postInfo.images!!.isNotEmpty()){ //이미지 존재하면
                binding.itemCommunityPostImage.visibility = View.VISIBLE
                // RecyclerView의 LayoutManager 및 Adapter 설정
                val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                binding.itemCommunityPostImage.layoutManager = layoutManager

                val imageAdapter = ItemFeedPhotoAdapter(postInfo.images!!)
                Log.d("커뮤니티이미지리스트", postInfo.images.toString())
                binding.itemCommunityPostImage.adapter = imageAdapter
            } else {
                // 이미지가 없는 경우 RecyclerView 숨기기
                binding.itemCommunityPostImage.visibility = View.GONE
            }

            // 아이템 제목 및 내용 클릭 시 상세 페이지로 이동
            binding.itemCommunityPostContainer.setOnClickListener {
                val intent = Intent(binding.root.context, CommunityDetailActivity::class.java)
                intent.putExtra("postId", postInfo.board_id)
                binding.root.context.startActivity(intent)
            }
            // 댓글 쓰기 클릭 시 상세 페이지로 이동
            binding.itemCommunityPostCommentTv.setOnClickListener {
                val intent = Intent(binding.root.context, CommunityDetailActivity::class.java)
                intent.putExtra("postId", postInfo.board_id)
                binding.root.context.startActivity(intent)
            }

            // 도와줘요 카테고리일 때만 요청중 icon 보이도록
            if(postInfo.category == "HELP"){
                binding.itemCommunityPostCategoryHelp.visibility = View.VISIBLE
            } else{
                binding.itemCommunityPostCategoryHelp.visibility = View.GONE
            }

//            // 좋아요 아이콘 클릭 리스너
//            binding.itemCommunityPostLikeIcon.setOnClickListener {
//                postInfo.isLiked = !postInfo.isLiked
//                select(postInfo.isLiked)
//            }
        }

        fun select(isLike: Boolean) {
            if (isLike) { // 좋아요 눌렀을 때의 반응
                binding.itemCommunityPostLikeIcon.setImageResource(R.drawable.icon_like)
            } else { // 누르지 않았을 때의 반응
                binding.itemCommunityPostLikeIcon.setImageResource(R.drawable.icon_unlike)
            }
        }
    }
}