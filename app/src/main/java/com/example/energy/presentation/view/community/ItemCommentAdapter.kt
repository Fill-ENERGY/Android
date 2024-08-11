package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.Comment
import com.example.energy.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ItemCommentAdapter(
    private val communityDetailActivity: CommunityDetailActivity,
    itemList: ArrayList<Comment>
) : RecyclerView.Adapter<ItemCommentAdapter.ItemCommentViewHolder>() {

    private lateinit var binding: ItemCommentBinding
    var itemSet = makeChildComment(itemList) //자식 정렬 알고리즘 호출
    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun addSubComment(comment: Comment)
//        fun userClick(userInfo: String)
        fun showDialog(comment: Comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCommentViewHolder {
        binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemCommentViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = itemSet.size


    inner class ItemCommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind() {
            val data = itemSet[absoluteAdapterPosition]
            val dataParent = data.parentCommentId?:-1
            if (dataParent != -1) { //자식 뷰
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT
                    ,ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(100,0,0,0)
                binding.itemComment.layoutParams = layoutParams //자식 뷰에 마진을 넣어줌

                // 이미지뷰 생성
                val imageView = ImageView(itemView.context)
                imageView.setImageResource(R.drawable.reply_vector)
                val imageParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                imageParams.setMargins(0, 0, 16, 0) // 텍스트와 이미지 사이 간격 설정
                imageView.layoutParams = imageParams

                // 이미지를 레이아웃에 추가
                (binding.root as LinearLayout).addView(imageView, 0) // 0 위치에 이미지를 추가
            }
            binding.commentText.text = data.body
            binding.commentUserName.text = data.userInfo

//            // 댓글 작성 시간 계산
//            val commentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(data.createTime)
//            val currentTime = Date() //현재 시간
//            if (commentTime != null) {
//                val diffInMillis = currentTime.time - commentTime.time
//                val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
//                binding.commentTimeText.text = if (diffInMinutes < 1) "방금 전" else "${diffInMinutes}분 전"
//            }

//            data.userInfo?.let { user ->
//                binding.commentUserName.text = user.id
//                if (user.profileFile != null) {
//                    Glide.with(mainActivity)
//                        .load(user.profileFile)
//                        .into(binding.userImage)
//                }
//                //유저 프로필 보기
//                binding.userImage.setOnClickListener {
//                    //User image click
//                    onItemClickListener?.userClick(user)
//                }
//            }

            // 대댓글 작성 버튼
            binding.commentWriteCommentBtn.setOnClickListener {
                onItemClickListener?.addSubComment(data)
            }

            // "더 보기" 아이콘 클릭 시 Dialog 띄우기
            binding.commentSeeMoreIcon.setOnClickListener {
                onItemClickListener?.showDialog(data)
            }
        }
    }
}