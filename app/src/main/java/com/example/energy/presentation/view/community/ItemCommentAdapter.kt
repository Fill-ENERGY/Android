package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.CommentModel
import com.example.energy.databinding.ActivityCommunityDetailBinding
import com.example.energy.databinding.ItemCommentBinding

class ItemCommentAdapter(private var itemList: List<CommentModel>) : RecyclerView.Adapter<ItemCommentAdapter.ItemCommentViewHolder>() {

    private lateinit var binding: ItemCommentBinding
    private lateinit var subBinding: ActivityCommunityDetailBinding
//    var itemSet = makeChildComment(itemList) //자식 정렬 알고리즘 호출
    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun addSubComment(commentModel: CommentModel)
//        fun userClick(userInfo: String)
        fun showDialog(commentModel: CommentModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCommentViewHolder {
        binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemCommentViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = itemList.size + itemList.sumOf { it.replies.size }


    inner class ItemCommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val (comment, isReply) = getCommentAtPosition(position)

            // 부모 댓글 또는 대댓글에 따른 레이아웃 조정
            if (isReply) {
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(100, 0, 0, 0) // 대댓글에 마진 추가
                binding.itemComment.layoutParams = layoutParams
            } else {
                val layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0, 0, 0, 0) // 부모 댓글에는 마진 없음
                binding.itemComment.layoutParams = layoutParams
            }

            // 댓글 내용과 작성자 이름 바인딩
            binding.commentText.text = comment.content
            binding.commentUserName.text = comment.memberName


//            val data = itemList[absoluteAdapterPosition]
//            Log.d("대댓글", "${data.replies[position]}")
//            val repliesData = data.replies[position]
//            val dataParent = repliesData.parentId
//
//
//            // 부모 댓글인지 대댓글인지 확인
//            if (data.replies[absoluteAdapterPosition].parentId != 0) { // 대댓글
//                val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                    ,ViewGroup.LayoutParams.WRAP_CONTENT)
//                layoutParams.setMargins(100,0,0,0)
//                binding.itemComment.layoutParams = layoutParams
//            }
//
//            binding.commentText.text = data.content
//            binding.commentUserName.text = data.memberName


            // 답글(대댓글) 작성 버튼
            binding.commentWriteCommentBtn.setOnClickListener {
                onItemClickListener?.addSubComment(comment)
                //subBinding.messageInput.requestFocus()  // 입력창에 포커스
            }

            // "더 보기" 아이콘 클릭 시 Dialog 띄우기
            binding.commentSeeMoreIcon.setOnClickListener {
                onItemClickListener?.showDialog(comment)
            }
        }

        // 전체 목록에서 주어진 위치의 댓글 또는 대댓글을 반환
        private fun getCommentAtPosition(position: Int): Pair<CommentModel, Boolean> {
            var currentPos = 0
            itemList.forEach { comment ->
                if (currentPos == position) {
                    return comment to false
                }
                currentPos++
                comment.replies.forEach { reply ->
                    if (currentPos == position) {
                        return reply to true
                    }
                    currentPos++
                }
            }
            throw IllegalStateException("Invalid position")
        }
    }
}