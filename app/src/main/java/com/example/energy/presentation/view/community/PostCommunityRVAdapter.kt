package com.example.energy.presentation.view.community

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.BoardModel
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.databinding.ItemCommunityFeedBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PostCommunityRVAdapter (private var postInfo: List<BoardModel>): RecyclerView.Adapter<PostCommunityRVAdapter.ViewHolder>() {

    interface PeopleItemClickListener {
        fun onItemClick(position: Int, community: BoardModel)
        fun onCheckIconClick(community: BoardModel)
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
            binding.itemCommunityPostUserName.text = postInfo.memberName
            binding.itemCommunityPostTitle.text = postInfo.title
            binding.itemCommunityPostContent.text = postInfo.content
            binding.itemCommunityPostLikeNum.text = postInfo.likeNum.toString()
            binding.itemCommunityPostCommentNum.text = postInfo.commentCount.toString()
            binding.itemCommunityPostCategoryView.setImageResource(fromString(postInfo.category))

            //토큰 가져오기
//            val sharedPreferences = binding.root.context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
//            val accessToken = sharedPreferences.getString("accessToken", "none")
            val accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODg3ODYzLCJleHAiOjE3MjY0Nzk4NjN9.qGR9PibGimGon0_82i_Z73nxXJzK1BDoPLWRLjC0QI4"

            var likeStatus: Boolean = false

//            // 좋아요 아이콘 설정
//            select(postInfo.isLiked)

            // 이미지 RecyclerView 설정
            if(postInfo.images.isNotEmpty()){ //이미지 존재하면
                binding.itemCommunityPostImage.visibility = View.VISIBLE
                // RecyclerView의 LayoutManager 및 Adapter 설정
                val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                binding.itemCommunityPostImage.layoutManager = layoutManager

                val imageAdapter = ItemFeedPhotoAdapter(postInfo.images)
                Log.d("커뮤니티이미지리스트", postInfo.images.toString())
                binding.itemCommunityPostImage.adapter = imageAdapter
            } else {
                // 이미지가 없는 경우 RecyclerView 숨기기
                binding.itemCommunityPostImage.visibility = View.GONE
            }

            // 아이템 제목 및 내용 클릭 시 상세 페이지로 이동
            binding.itemCommunityPostContainer.setOnClickListener {
                val intent = Intent(binding.root.context, CommunityDetailActivity::class.java)
                intent.putExtra("postId", postInfo.id)
                intent.putExtra("likeStatus", likeStatus)
                binding.root.context.startActivity(intent)
            }
            // 댓글 쓰기 클릭 시 상세 페이지로 이동
            binding.itemCommunityPostCommentTv.setOnClickListener {
                val intent = Intent(binding.root.context, CommunityDetailActivity::class.java)
                intent.putExtra("postId", postInfo.id)
                intent.putExtra("likeStatus", likeStatus)
                binding.root.context.startActivity(intent)
            }

            // 도와줘요 카테고리일 때만 요청중 icon 보이도록
            if(postInfo.category == "HELP"){
                binding.itemCommunityPostCategoryHelp.visibility = View.VISIBLE
            } else{
                binding.itemCommunityPostCategoryHelp.visibility = View.GONE
            }

            // 좋아요 아이콘 클릭 리스너
            binding.itemCommunityPostLikeIcon.setOnClickListener {
                likeStatus = !likeStatus

                if(likeStatus){
                    // 서버에 좋아요 상태 업데이트 요청
                    CommunityRepository.postLikeBoard(accessToken, postInfo.id) { response ->
                        if (response != null) {
                            // 좋아요 상태 업데이트 성공 시
                            postInfo.likeNum = response.likeCount
                            likeStatus = true
                            select(likeStatus)
                            binding.itemCommunityPostLikeNum.text = postInfo.likeNum.toString() //업데이트..
                        } else {
                            // 좋아요 상태 업데이트 실패 시
                            Log.d("커뮤니티좋아요상태변경", "좋아요 상태 업데이트 실패")
                        }
                    }
                } else{
                    // 서버에 좋아요 삭제 업데이트 요청
                    CommunityRepository.deleteLikeBoard(accessToken, postInfo.id) { response ->
                        if (response != null) {
                            // 좋아요 상태 업데이트 성공 시
                            postInfo.likeNum = response.likeCount
                            likeStatus = false
                            select(likeStatus)
                            binding.itemCommunityPostLikeNum.text = postInfo.likeNum.toString() //업데이트..
                        } else {
                            // 좋아요 상태 업데이트 실패 시
                            Log.d("커뮤니티좋아요상태변경", "좋아요 상태 업데이트 실패")
                        }
                    }
                }

                //toggleLike(postInfo, postInfo.id, postInfo.likeNum, accessToken)
            }

//            // 커뮤니티 경과 시간 bind
//            val timeText = checkMinute(postInfo)
//            binding.itemCommunityPostUploadTime.text = timeText
        }

        // 좋아요 기능 함수
        fun toggleLike(postInfo: BoardModel, boardId: Int, likeCount: Int, accessToken: String) {
            // 현재 좋아요 상태를 토글 (이미 좋아요를 눌렀으면 false, 아니면 true)
            val newLikeStatus = likeCount == 0
            val newLikeCount = if (newLikeStatus) likeCount + 1 else likeCount - 1

            // 서버에 좋아요 상태 업데이트 요청
            CommunityRepository.postLikeBoard(accessToken, boardId) { response ->
                if (response != null) {
                    // 좋아요 상태 업데이트 성공 시
                    postInfo.likeNum = response.likeCount ?: newLikeCount
                    select(newLikeStatus)
                    binding.itemCommunityPostLikeTv.text = postInfo.likeNum.toString() //업데이트..
                } else {
                    // 좋아요 상태 업데이트 실패 시
                    Log.d("커뮤니티좋아요상태변경", "좋아요 상태 업데이트 실패")
                }
            }

            // 서버에 좋아요 삭제 업데이트 요청
            CommunityRepository.deleteLikeBoard(accessToken, boardId) { response ->
                if (response != null) {
                    // 좋아요 상태 업데이트 성공 시
                    postInfo.likeNum = response.likeCount ?: newLikeCount
                    select(newLikeStatus)
                    binding.itemCommunityPostLikeTv.text = postInfo.likeNum.toString() //업데이트..
                } else {
                    // 좋아요 상태 업데이트 실패 시
                    Log.d("커뮤니티좋아요상태변경", "좋아요 상태 업데이트 실패")
                }
            }
        }

        fun select(isLike: Boolean) {
            if (isLike) { // 좋아요 눌렀을 때의 반응
                binding.itemCommunityPostLikeIcon.setImageResource(R.drawable.icon_like)
            } else { // 누르지 않았을 때의 반응
                binding.itemCommunityPostLikeIcon.setImageResource(R.drawable.icon_unlike)
            }
        }


        fun checkMinute(postInfo: BoardModel): String { // 게시글 post 시간 계산하는 함수
            // 현재 시간 가져오기
            var current = Calendar.getInstance().time

            // 서버에서 받은 시간 포맷과 동일하게 SimpleDateFormat 정의
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            // postInfo의 createdAt 문자열을 Date로 변환
            val postTime = formatter.parse(postInfo.createdAt)
            current = formatter.parse(current.toString())

//            var postTime: Date? = null
//
//            try {
//                postTime = formatter.parse(postInfo.createdAt)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                return "시간 형식 오류" // 파싱 오류 발생 시 반환값
//            }
//
//            if (postTime == null) {
//                return "시간 데이터 없음"
//            }

            // 두 시간 사이의 차이를 계산
            val durationInMillis = current.time - postTime!!.time
            val durationInDays = durationInMillis / (1000 * 60 * 60 * 24)
            val durationInMinutes = durationInMillis / (1000 * 60)

            return when {
                durationInMinutes < 1 -> "방금 전"
                durationInDays >= 1 -> {
                    // 하루 이상 차이가 나는 경우, yy.MM.dd 형식으로 반환
                    val dateFormatter = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
                    dateFormatter.format(postTime)
                }
                else -> {
                    // 하루 미만의 경우, 경과된 분 수를 반환
                    "$durationInMinutes 분 전"
                }
            }
        }
    }
}