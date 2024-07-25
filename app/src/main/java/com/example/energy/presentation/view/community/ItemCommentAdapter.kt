package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.community.Comment
import com.example.energy.databinding.ItemCommentBinding

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
            }
            binding.commentText.text = data.body
//            binding.commentTimeText.text = getTimeText(data.createTime)

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

            // 댓글 작성 버튼
            binding.commentWriteCommentBtn.setOnClickListener {
                onItemClickListener?.addSubComment(data)
            }
        }
    }
}