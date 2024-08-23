package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.community.BoardModel
import com.example.energy.data.repository.community.CommentModel
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.data.repository.community.HelpStatusRequest
import com.example.energy.data.repository.community.PostBoardRequest
import com.example.energy.data.repository.community.WriteCommentRequest
import com.example.energy.data.repository.note.ChatThreadsResponse
import com.example.energy.data.repository.note.MessageResponse
import com.example.energy.data.repository.note.NoteRepository
import com.example.energy.data.repository.note.RecentMessage
import com.example.energy.databinding.ActivityCommunityDetailBinding
import com.example.energy.databinding.DialogCommunityBlockBinding
import com.example.energy.databinding.DialogCommunityCommentWriterSeeMoreBinding
import com.example.energy.databinding.DialogCommunityUserSeeMoreBinding
import com.example.energy.databinding.DialogCommunityWriterSeeMoreBinding
import com.example.energy.databinding.DialogHelpStatusBinding
import com.example.energy.databinding.DialogLoadingBinding
import com.example.energy.databinding.DialogPostCommunitySuccessBinding
import com.example.energy.presentation.view.note.NoteLiveChatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.internal.ViewUtils.hideKeyboard

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
    private var selectedComment: CommentModel? = null // 선택된 댓글 객체를 추적
    private var writerId: Int? = null
    private var loadingDialog: Dialog? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //토큰 가져오기
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        accessToken = sharedPreferences?.getString("accessToken", "none")

        // 인텐트로부터 전달받은 postId(board_id) 가져옴. 기본값은 -1로 설정하여 예외처리
        postId = intent.getIntExtra("postId", -1)
        writerId = intent.getIntExtra("memberId", -1)
        Log.d("게시글아이디", "${postId}")

        // 댓글 전송 버튼
        binding.sendButton.setOnClickListener {
            val commentText = binding.messageInput.text.toString()
            if(commentText.isNotBlank()){

                Log.d("댓글!!!", "${selectedComment}")
                Log.d("댓글잠금상태", "${isSecret}")
                Log.d("댓글내용", "${commentText}")

                if(selectedComment?.parentId != 0){
                    parentCommentId = selectedComment?.parentId
                } else{
                    parentCommentId = selectedComment?.id
                }

                Log.d("부모댓글아이디", "${parentCommentId}")

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
                        selectedComment = null  // 대댓글 작성이 끝났으므로 selectedComment 초기화
                        refreshData() //데이터 재로딩
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
                selectedComment = null
            }
            false
        }

        // 뒤로가기 버튼
        binding.communityDetailBackIcon.setOnClickListener {
            finish() //현재 Activity 종료
        }



        // 채팅하기 버튼 클릭 시
        binding.communityDetailChattingBtn.setOnClickListener {
            NoteRepository.communityGetMessages(accessToken!!, writerId!!) { response ->

                if (response != null) {
                    Log.d("커뮤니티채팅버튼","채팅으로 이동 성공  ${writerId}")

                    //if (response.result?.threadId == null) {

                    //}

                    val intent = Intent(this, NoteLiveChatActivity::class.java)

                    intent.putExtra("Username", writerId)
                    intent.putExtra("Id","dfdkssf")
                    intent.putExtra("threadId", writerId)
                    intent.putExtra("receiverId", writerId)


                    //intent.putExtra("cursor", )



                    startActivity(intent)



                }
            }
        }
    }





    override fun onResume() {
        super.onResume()
        showLoading() //데이터 로딩 페이지
        // API를 다시 호출하여 데이터를 갱신
        refreshData()
    }

    private fun refreshData() {
        // 커뮤니티 댓글 조회 api
        CommunityRepository.getListComment(accessToken!!, postId) {
                response ->
            response.let {
                Log.d("게시글댓글정보", "${response}")
                //통신성공
                hideLoading()
                if (response != null) {
                    // 댓글 RecyclerView 연결
                    commentAdapter = ItemCommentAdapter(response, writerId)
                    binding.communityDetailCommentView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.communityDetailCommentView.adapter = commentAdapter

                    // 댓글 Adapter에 리스너 설정
                    commentAdapter.onItemClickListener = object : ItemCommentAdapter.OnItemClickListener {
                        override fun addSubComment(commentModel: CommentModel) {
                            // 대댓글 추가 로직 구현
                            // 부모 댓글 ID를 설정하여 자식 댓글을 작성할 준비
                            selectedComment = commentModel
                            binding.messageInput.requestFocus()  // 입력창에 포커스
                        }

                        override fun showDialog(commentModel: CommentModel) {
                            if(commentModel.author){ //댓글 작성자 Dialog
                                showCommentWriterDialog(commentModel)
                            } else{ //일반 유저 Dialog
                                showCommentUserDialog(commentModel)
                            }
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
                hideLoading()
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


                // 채팅하기 버튼 클릭 시
                binding.communityDetailChattingBtn.setOnClickListener {
                    NoteRepository.communityGetMessages(accessToken!!, writerId!!) { responseMessage ->

                        if (responseMessage != null) {
                            Log.d("커뮤니티채팅버튼","채팅으로 이동 성공  ${writerId}")



                            val intent = Intent(this, NoteLiveChatActivity::class.java)

                            intent.putExtra("Username", response.memberName)
                            intent.putExtra("Id","fillenergy")
                            intent.putExtra("threadId", responseMessage.result?.threadId)
                            intent.putExtra("receiverId", writerId)
                            intent.putExtra("unreadMessageCount", 0)
                            intent.putExtra("cursor", 0)


                            startActivity(intent)







                        }
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
                }

                // 도와줘요 카테고리인 경우 & 일반 사용자일 경우
                if(response.category == "HELP" && response.author == false){
                    binding.communityDetailHelpCategory.visibility = View.VISIBLE
                } else{
                    binding.communityDetailHelpCategory.visibility = View.GONE
                }

                //더보기 클릭 리스너
                if(response.author){ //작성자인 경우
                    // 더보기 버튼
                    binding.communityDetailSeeMore.setOnClickListener {
                        showSeeMoreWriterDialog()
                    }
                } else{
                    // 더보기 버튼
                    binding.communityDetailSeeMore.setOnClickListener {
                        showSeeMoreUserDialog()
                    }
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
                    refreshData()
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
                    refreshData()
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
                    refreshData()
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

        // 게시글 수정하기
        binding.dialogCommunityEdit.setOnClickListener {
            val intent = Intent(binding.root.context, CommunityWritingActivity::class.java)
            intent.putExtra("postId", postId)
            binding.root.context.startActivity(intent)
            bottomSheetDialog.dismiss() //Dialog 닫기
        }

        // 게시글 삭제하기
        binding.dialogCommunityDelete.setOnClickListener {
            CommunityRepository.deleteBoard(accessToken!!, postId) { response  ->
                if(response != null){
                    Log.d("게시글삭제", "게시글 삭제 성공: ${response.result}")
                }else {
                    // 상태 변경 실패
                    Log.e("게시글삭제", "게시글 삭제 실패: ${response}")
                }
            }
            bottomSheetDialog.dismiss() //Dialog 닫기
        }

        bottomSheetDialog.show()
    }

    private fun showSeeMoreUserDialog() { // 일반 사용자 더보기 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogCommunityUserSeeMoreBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        // 게시글 차단 버튼
        binding.dialogCommunityBlock.setOnClickListener {
            bottomSheetDialog.dismiss() //Dialog 닫기
            showBlockDialog()
        }

        // 게시글 신고 버튼
        binding.dialogCommunityReport.setOnClickListener {
            bottomSheetDialog.dismiss() //Dialog 닫기
        }

        bottomSheetDialog.show()
    }

    private fun showCommentWriterDialog(commentModel: CommentModel){ // 댓글 작성자 더보기 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogCommunityCommentWriterSeeMoreBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        // 댓글 수정 버튼
        binding.dialogCommentEdit.setOnClickListener {
            val intent = Intent(binding.root.context, CommentEditActivity::class.java)
            intent.putExtra("postId", postId)
            intent.putExtra("commentId", commentModel.id)
            binding.root.context.startActivity(intent)

            bottomSheetDialog.dismiss() //Dialog 닫기
        }

        // 댓글 삭제 버튼
        binding.dialogCommentDelete.setOnClickListener {
            CommunityRepository.deleteComment(accessToken!!, postId, commentModel.id) { response  ->
                if(response != null){
                    Log.d("댓글삭제", "댓글 삭제 성공: ${response.result}")
                    refreshData() //데이터 재로딩
                }else {
                    // 상태 변경 실패
                    Log.e("댓글삭제", "댓글 삭제 실패: ${response}")
                }
            }
            bottomSheetDialog.dismiss() //Dialog 닫기
        }

        bottomSheetDialog.show()
    }

    private fun showCommentUserDialog(commentModel: CommentModel){ // 댓글 일반 유저 더보기 Dialog
        val bottomSheetDialog = BottomSheetDialog(this)
        val binding = DialogCommunityUserSeeMoreBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(binding.root)

        // 댓글 차단 버튼
        binding.dialogCommunityBlock.setOnClickListener {
            bottomSheetDialog.dismiss() //Dialog 닫기
            showBlockDialog()
        }

        // 댓글 신고 버튼
        binding.dialogCommunityReport.setOnClickListener {
            bottomSheetDialog.dismiss() //Dialog 닫기
        }

        bottomSheetDialog.show()
    }


    private fun showBlockDialog() { // 차단 Dialog
        val dialog = Dialog(this, R.style.CustomDialog)
        val binding = DialogCommunityBlockBinding.inflate(layoutInflater)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 기존 다이어그램 배경 투명으로 적용(커스텀한 배경이 보이게 하기 위함)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false) // 바깥 영역 터치해도 닫힘 X

        binding.dialogCommunityBlockYes.setOnClickListener {
            // '예' 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
        }

        binding.dialogCommunityBlockNo.setOnClickListener {
            // '아니오' 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
        }

        // 다이얼로그 표시
        dialog.show()
    }

    fun updateLikeIcon(isLike: Boolean) { //좋아요 UI 업데이트 함수
        if (isLike) { // 좋아요 눌렀을 때의 반응
            binding.communityDetailLikeIcon.setImageResource(R.drawable.icon_like)
        } else { // 누르지 않았을 때의 반응
            binding.communityDetailLikeIcon.setImageResource(R.drawable.icon_unlike)
        }
    }

    private fun showLoading() { //데이터 로딩 페이지 함수
        if (loadingDialog == null) {
            loadingDialog = Dialog(this, R.style.LoadingDialog)
            val binding = DialogLoadingBinding.inflate(layoutInflater) // 로딩 레이아웃 바인딩
            loadingDialog?.setContentView(binding.root)
            loadingDialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ) // 다이얼로그 크기를 전체 화면으로 설정
            loadingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loadingDialog?.setCanceledOnTouchOutside(false) // 바깥 영역 터치해도 닫힘 X
        }
        loadingDialog?.show()
    }

    private fun hideLoading() { //로딩 페이지 숨기는 함수
        loadingDialog?.dismiss()
    }
}