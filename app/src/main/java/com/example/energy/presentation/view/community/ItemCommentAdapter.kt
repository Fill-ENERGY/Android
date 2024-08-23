package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.BoardModel
import com.example.energy.data.repository.community.CommentModel
import com.example.energy.databinding.ActivityCommunityDetailBinding
import com.example.energy.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ItemCommentAdapter(private var itemList: List<CommentModel>, private var boardWriterInfo: Int? = null) : RecyclerView.Adapter<ItemCommentAdapter.ItemCommentViewHolder>() {

    private lateinit var binding: ItemCommentBinding
    private lateinit var subBinding: ActivityCommunityDetailBinding
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


            // 게시글 작성자와 댓글 작성자가 동일한 경우ㅎ
            if (boardWriterInfo == comment.memberId) {
                // 일반 유저 시점
                binding.commentWriter.visibility = View.VISIBLE

                // 작성자가 유저 본인일 때
                if (comment.author) {
                    binding.commentUserName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.main_orange))
                    binding.commentWriter.setTextColor(ContextCompat.getColor(binding.root.context, R.color.main_orange))
                } else {
                    binding.commentUserName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_scale8))
                    binding.commentWriter.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_scale8))
                }
            } else {
                binding.commentWriter.visibility = View.GONE
                binding.commentUserName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_scale8))
                binding.commentWriter.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_scale8))
            }


            // 답글(대댓글) 작성 버튼
            binding.commentWriteCommentBtn.setOnClickListener {
                onItemClickListener?.addSubComment(comment)
                //subBinding.messageInput.requestFocus()  // 입력창에 포커스
            }

            // "더 보기" 아이콘 클릭 시 Dialog 띄우기
            binding.commentSeeMoreIcon.setOnClickListener {
                onItemClickListener?.showDialog(comment)
            }

            // 댓글 경과 시간 bind
            var timeText = checkMinute(comment)
            binding.commentTimeText.text = timeText
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


        fun checkMinute(commentInfo: CommentModel): String {
            // 현재 시간 가져오기
            val current = Calendar.getInstance().time

            // 서버에서 받은 시간 포맷과 동일하게 SimpleDateFormat 정의
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            // postInfo의 createdAt 문자열을 Date로 변환
            val commentTime: Date?
            try {
                commentTime = formatter.parse(commentInfo.createdAt)
            } catch (e: Exception) {
                e.printStackTrace()
                return "시간 형식 오류" // 파싱 오류 발생 시 반환값
            }

            if (commentTime == null) {
                return "시간 데이터 없음"
            }

            // 두 시간 사이의 차이를 계산
            val durationInMillis = current.time - commentTime.time
            val durationInMinutes = durationInMillis / (1000 * 60)
            val durationInHours = durationInMinutes / 60
            val durationInDays = durationInMillis / (1000 * 60 * 60 * 24)

            return when {
                durationInMinutes < 1 -> "방금 전"
                durationInHours >= 24 -> {
                    // 하루 이상 차이가 나는 경우, yy.MM.dd 형식으로 반환
                    val dateFormatter = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
                    dateFormatter.format(commentTime)
                }
                durationInMinutes >= 60 -> {
                    "${durationInHours}시간 전"
                }
                else -> {
                    // 하루 미만의 경우, 경과된 분 수를 반환
                    "${durationInMinutes}분 전"
                }
            }
        }
    }
}