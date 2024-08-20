package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.community.BoardModel
import com.example.energy.data.repository.community.CommentModel
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.data.repository.community.HelpStatusRequest
import com.example.energy.data.repository.community.PostBoardRequest
import com.example.energy.data.repository.community.WriteCommentRequest
import com.example.energy.databinding.ActivityCommunityDetailBinding
import com.example.energy.databinding.DialogCommunityCommentSeeMoreBinding
import com.example.energy.databinding.DialogCommunityUserSeeMoreBinding
import com.example.energy.databinding.DialogCommunityWriterSeeMoreBinding
import com.example.energy.databinding.DialogHelpStatusBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CommunityDetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var commentAdapter: ItemCommentAdapter
    private var parentCommentId: Int? = null // 자식 댓글의 부모 댓글 ID
    private var writerStatus: String = "" //기본 값
    private var imageList: List<String>? = null
    private var status : String = ""
    private var accessToken: String? = null
    private var likeStatus: Boolean = false  // 좋아요 상태를 저장하는 변수
    private var likeCount: Int = 0 // 좋아요 개수를 저장하는 변수
    private var isSecret: Boolean = false  // 좋아요 상태를 저장하는 변수
    private var postId: Int = 0

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //토큰 가져오기
//        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
//        var accessToken = sharedPreferences?.getString("accessToken", "none")
        accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzOTg1OTUxLCJleHAiOjE3MjY1Nzc5NTF9.jEn8OyBau-JQ576OLgESOD0dGcGH614WfsQUGGbtq_M"

        // 인텐트로부터 전달받은 postId(board_id) 가져옴. 기본값은 -1로 설정하여 예외처리
        postId = intent.getIntExtra("postId", -1)
        Log.d("게시글아이디", "${postId}")

        // 커뮤니티 댓글 조회 api
        CommunityRepository.getListComment(accessToken!!, postId) {
                response ->
            response.let {
                Log.d("게시글댓글정보", "${response}")
                //통신성공
                if (response != null) {
                    // 댓글 RecyclerView 연결
                    commentAdapter = ItemCommentAdapter(response)
                    binding.communityDetailCommentView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.communityDetailCommentView.adapter = commentAdapter

                    // 댓글 Adapter에 리스너 설정
                    commentAdapter.onItemClickListener = object : ItemCommentAdapter.OnItemClickListener {
                        override fun addSubComment(commentModel: CommentModel) {
                            // 대댓글 추가 로직 구현
                            // 부모 댓글 ID를 설정하여 자식 댓글을 작성할 준비
                            parentCommentId = commentModel.id
                            binding.messageInput.requestFocus()  // 입력창에 포커스
                        }

                        override fun showDialog(commentModel: CommentModel) {
                            showCommentDialog(commentModel)
                        }
                    }
                } else {
                    Log.e("커뮤니티댓글조회api테스트", "응답 결과가 null이거나 comment가 없습니다.")
                }
            }
        }


        // 상세 게시글 조회 API 호출
        CommunityRepository.getDetailCommunity(accessToken!!, postId) { response ->
            if (response != null) {
                //통신성공
                var isLiked = response.liked
                var likeCount = response.likeNum
                writerStatus = response.helpStatus

                //binding.communityDetailUserProfile.setImageResource(postInfo.userProfile!!)
                binding.communityDetailUserName.text = response.memberName
                binding.communityDetailTitle.text = response.title
                binding.communityDetailContent.text = response.content
                binding.communityDetailCommentNum.text = response.commentCount.toString()
                binding.communityDetailCategoryTitle.text = toKorean(response.category)
                updateLikeIcon(isLiked)
                binding.communityDetailLikeNum.text = response.likeNum.toString()
                when (writerStatus) {
                    "RESOLVED" -> {
                        binding.communityDetailWriterRequestBtn.setImageResource(R.drawable.button_resolved)
                        binding.communityDetailHelpCategory.setImageResource(R.drawable.icon_tag_resolved)
                    }
                    "IN_PROGRESS" -> {
                        binding.communityDetailWriterRequestBtn.setImageResource(R.drawable.button_contacting)
                        binding.communityDetailHelpCategory.setImageResource(R.drawable.icon_tag_contacting)
                    }
                    else -> {
                        binding.communityDetailWriterRequestBtn.setImageResource(R.drawable.button_requesting)
                        binding.communityDetailHelpCategory.setImageResource(R.drawable.icon_tag_requesting)
                    }
                }

                // 좋아요 아이콘 클릭 시
                binding.communityDetailLikeIcon.setOnClickListener {
                    isLiked = !isLiked

                    if(isLiked){
                        // 서버에 좋아요 상태 업데이트 요청
                        CommunityRepository.postLikeBoard(accessToken!!, response.id) { response ->
                            if (response != null) {
                                // 좋아요 상태 업데이트 성공 시
                                isLiked = response.liked
                                likeCount = response.likeCount
                                updateLikeIcon(isLiked)
                                binding.communityDetailLikeNum.text = likeCount.toString() //업데이트..
                            } else {
                                // 좋아요 상태 업데이트 실패 시
                                Log.d("커뮤니티좋아요상태변경", "좋아요 상태 업데이트 실패")
                            }
                        }
                    } else{
                        // 서버에 좋아요 삭제 업데이트 요청
                        CommunityRepository.deleteLikeBoard(accessToken!!, response.id) { response ->
                            if (response != null) {
                                // 좋아요 상태 업데이트 성공 시
                                isLiked = response.liked
                                likeCount = response.likeCount
                                updateLikeIcon(isLiked)
                                binding.communityDetailLikeNum.text = likeCount.toString() //업데이트..
                            } else {
                                // 좋아요 상태 업데이트 실패 시
                                Log.d("커뮤니티좋아요상태변경", "좋아요 상태 업데이트 실패")
                            }
                        }
                    }
                }

                Log.d("커뮤니티이미지리스트2", response.images.toString())

                // 이미지 RecyclerView 설정
                if(response.images.isEmpty()){
                    // 이미지가 없는 경우 RecyclerView 숨기기
                    binding.communityDetailImage.visibility = View.GONE
                } else{
                    binding.communityDetailImage.visibility = View.VISIBLE
                    // RecyclerView의 LayoutManager 및 Adapter 설정
                    val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                    binding.communityDetailImage.layoutManager = layoutManager

                    val imageAdapter = ItemFeedPhotoAdapter(response.images)
                    Log.d("커뮤니티이미지리스트2", response.images.toString())
                    binding.communityDetailImage.adapter = imageAdapter
                }

                // 도와줘요 카테고리 & 작성자일 경우
                if(response.category == "HELP" && response.author){
                    binding.communityDetailChattingBtn.visibility = View.GONE
                    binding.communityDetailWriterRequestBtn.visibility = View.VISIBLE

                    // 더보기 버튼
                    binding.communityDetailSeeMore.setOnClickListener {
                        showSeeMoreWriterDialog()
                    }
                }

                // 도와줘요 카테고리인 경우 & 일반 사용자일 경우
                if(response.category == "HELP" && response.author == false){
                    binding.communityDetailSeeMore.visibility = View.VISIBLE

                        // 더보기 버튼
                        binding.communityDetailSeeMore.setOnClickListener {
                            showSeeMoreUserDialog()
                        }
                } else{
                    binding.communityDetailHelpCategory.visibility = View.GONE
                }

                // 잠금 버튼 클릭 시 (댓글)
                binding.unlockButton.setOnClickListener {
                    isSecret = !isSecret
                    if(isSecret){
                        binding.unlockButton.setImageResource(R.drawable.icon_lock)
                    } else{
                        binding.unlockButton.setImageResource(R.drawable.icon_unlock)
                    }
                }


                // 작성자의 요청 상태 결정 Dialog
                binding.communityDetailWriterRequestBtn.setOnClickListener {
                    writerStatus = showHelpStatusDialog(writerStatus)
                }


            } else {
                // 데이터가 null인 경우 처리
                Log.e("상세커뮤니티조회", "상세 게시글 데이터가 없습니다. response: $response")
            }
        }

        // 댓글 전송 버튼
        binding.sendButton.setOnClickListener {
            val commentText = binding.messageInput.text.toString()
            if(commentText.isNotBlank()){

                Log.d("댓글잠금상태", "${isSecret}")
                Log.d("댓글내용", "${commentText}")
                Log.d("댓글아이디", "${parentCommentId}")

                // 댓글 작성 요청 데이터
                val writeCommentRequest = WriteCommentRequest(
                    content = commentText,
                    secret = isSecret,
                    parentCommentId = parentCommentId,
                    images = emptyList(),
                )

                // 댓글 API 호출
                CommunityRepository.writeCommentBoard(accessToken!!, postId, writeCommentRequest) { response  ->
                    if (response != null) {
                        // 성공적으로 댓글 작성됨
                        Log.d("댓글업로드", "댓글 작성 성공: ${response.content}")

                        // 댓글 목록에 추가하고 RecyclerView 업데이트
                        commentAdapter.notifyDataSetChanged()

                        // 댓글 입력 필드 초기화
                        binding.messageInput.text.clear()
                        parentCommentId = 0  // 대댓글 작성이 끝났으므로 parentCommentId 초기화
                    } else {
                        // 댓글 작성 실패
                        Log.e("댓글업로드", "댓글 작성 실패: ${response}")
                    }
                }
            }
        }

        // 키보드 외부 화면 클릭 시 키보드 숨기기
        binding.communityDetail.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        // 뒤로가기 버튼
        binding.communityDetailBackIcon.setOnClickListener {
            finish() //현재 Activity 종료
        }
    }

    fun toKorean(category: String): String{
        return when(category){
            "DAILY" -> "일상"
            "INQUIRY" -> "궁금해요"
            "HELP" -> "도와줘요"
            "WHEELCHAIR" -> "휠체어"
            else -> "스쿠터"
        }
    }

//    // 댓글 추가 기능
//    private fun addCommentToDatabase(comment: Comment) {
//        Thread {
//            communityDB.commentDao().insertComment(comment)
//            runOnUiThread {
//                updateCommentsList(comment)
//            }
//        }.start()
//    }

//    // Comment 업데이트 함수
//    private fun updateCommentsList(newComment: Comment) {
////        val fragmentManager: FragmentManager = supportFragmentManager
////        val communityWholeFragment = fragmentManager.findFragmentByTag("CommunityWholeFragment") as CommunityWholeFragment?
////        communityWholeFragment?.updatePostList(newComment)
//        dataList.add(newComment)
//        commentAdapter.itemSet = makeChildComment(dataList)
//        commentAdapter.notifyDataSetChanged()
//    }

    // 키보드 내리는 함수
    private fun hideKeyboard() {
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocusView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun showHelpStatusDialog(writerStatus: String): String { // 도와줘요 요청 상태 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogHelpStatusBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)
        var status: String = writerStatus
        Log.d("현재상태", "${status}")

        // 초기 상태에 따라 버튼 색상 설정
        when (writerStatus) {
            "REQUESTED" -> {
                binding.dialogHelpRequest.setBackgroundResource(R.drawable.select_help_status)
                binding.dialogHelpRequest.setTextColor(getColor(R.color.white))

                binding.dialogHelpContacting.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpContacting.setTextColor(getColor(R.color.gray_scale6))

                binding.dialogHelpResolved.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpResolved.setTextColor(getColor(R.color.gray_scale6))
            }
            "IN_PROGRESS" -> {
                binding.dialogHelpRequest.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpRequest.setTextColor(getColor(R.color.gray_scale6))

                binding.dialogHelpContacting.setBackgroundResource(R.drawable.select_help_status)
                binding.dialogHelpContacting.setTextColor(getColor(R.color.white))

                binding.dialogHelpResolved.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpResolved.setTextColor(getColor(R.color.gray_scale6))
            }
            "RESOLVED" -> {
                binding.dialogHelpRequest.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpRequest.setTextColor(getColor(R.color.gray_scale6))

                binding.dialogHelpContacting.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpContacting.setTextColor(getColor(R.color.gray_scale6))

                binding.dialogHelpResolved.setBackgroundResource(R.drawable.select_help_status)
                binding.dialogHelpResolved.setTextColor(getColor(R.color.white))
            }
            else -> {
                // 기본 상태
                binding.dialogHelpRequest.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpRequest.setTextColor(getColor(R.color.gray_scale6))

                binding.dialogHelpContacting.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpContacting.setTextColor(getColor(R.color.gray_scale6))

                binding.dialogHelpResolved.setBackgroundResource(R.drawable.unselect_help_status)
                binding.dialogHelpResolved.setTextColor(getColor(R.color.gray_scale6))
            }
        }

        //요청 중
        binding.dialogHelpRequest.setOnClickListener {
            // 상태 변경 요청 데이터
            val helpStatusRequest = HelpStatusRequest(
                helpStatus = "REQUESTED"
            )

            CommunityRepository.helpStatus(accessToken!!, postId, helpStatusRequest) { response  ->
                if(response != null){
                    status = response.helpStatus
                    Log.d("상태변경", "상태 변경 성공: ${response.helpStatus}")
                }else {
                    // 상태 변경 실패
                    Log.e("상태변경", "상태 변경 실패: ${response}")
                }
            }

            bottomSheetDialog.dismiss()
        }

        //연락 중
        binding.dialogHelpContacting.setOnClickListener {
            // 상태 변경 요청 데이터
            val helpStatusRequest = HelpStatusRequest(
                helpStatus = "IN_PROGRESS"
            )

            CommunityRepository.helpStatus(accessToken!!, postId, helpStatusRequest) { response  ->
                if(response != null){
                    status = response.helpStatus
                    Log.d("상태변경", "상태 변경 성공: ${response.helpStatus}")
                }else {
                    // 상태 변경 실패
                    Log.e("상태변경", "상태 변경 실패: ${response}")
                }
            }

            bottomSheetDialog.dismiss()
        }

        //해결 완료
        binding.dialogHelpResolved.setOnClickListener {
            // 상태 변경 요청 데이터
            val helpStatusRequest = HelpStatusRequest(
                helpStatus = "RESOLVED"
            )

            CommunityRepository.helpStatus(accessToken!!, postId, helpStatusRequest) { response  ->
                if(response != null){
                    status = response.helpStatus
                    Log.d("상태변경", "상태 변경 성공: ${response.helpStatus}")
                }else {
                    // 상태 변경 실패
                    Log.e("상태변경", "상태 변경 실패: ${response}")
                }
            }

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
        return status
    }

    private fun showSeeMoreWriterDialog() { // 작성자 더보기 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogCommunityWriterSeeMoreBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        binding.dialogCommunityEdit.setOnClickListener {
            val intent = Intent(binding.root.context, CommunityWritingActivity::class.java)
            intent.putExtra("postId", postId)
            binding.root.context.startActivity(intent)
        }

        bottomSheetDialog.show()
    }

    private fun showSeeMoreUserDialog() { // 일반 사용자 더보기 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogCommunityUserSeeMoreBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        bottomSheetDialog.show()
    }

    private fun showCommentDialog(commentModel: CommentModel){ // 댓글 더보기 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogCommunityCommentSeeMoreBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        // 댓글 수정 버튼
        binding.dialogCommentEdit.setOnClickListener {
//            val intent = Intent(binding.root.context, CommentEditActivity::class.java)
//            intent.putExtra("postId", postInfo.id)
//            binding.root.context.startActivity(intent)
            startActivity(Intent(this, CommentEditActivity::class.java))
        }

        // 댓글 삭제 버튼
        binding.dialogCommentDelete.setOnClickListener {

        }

        bottomSheetDialog.show()
    }

    fun updateLikeIcon(isLike: Boolean) { //좋아요 UI 업데이트 함수
        if (isLike) { // 좋아요 눌렀을 때의 반응
            binding.communityDetailLikeIcon.setImageResource(R.drawable.icon_like)
        } else { // 누르지 않았을 때의 반응
            binding.communityDetailLikeIcon.setImageResource(R.drawable.icon_unlike)
        }
    }
}