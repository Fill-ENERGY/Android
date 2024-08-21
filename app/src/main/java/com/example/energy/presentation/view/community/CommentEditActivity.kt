package com.example.energy.presentation.view.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.energy.R
import com.example.energy.data.repository.community.CommentModel
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.data.repository.community.PostBoardRequest
import com.example.energy.data.repository.community.UpdateCommentRequest
import com.example.energy.databinding.ActivityCommunityCommentEditBinding

class CommentEditActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCommunityCommentEditBinding
    private var accessToken: String? = null
    private var postId: Int = 0
    private var commentId: Int = 0
    private var imageList: List<String>? = null
    private var isSecret: Boolean = false  // 좋아요 상태를 저장하는 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityCommentEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //토큰 가져오기
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        accessToken = sharedPreferences?.getString("accessToken", "none")

        // 인텐트로부터 전달받은 commentId 가져옴. 기본값은 -1로 설정하여 예외처리
        postId = intent.getIntExtra("postId", -1)
        commentId = intent.getIntExtra("commentId", -1)

        loadCommentData(commentId)

        // 키보드 외부 화면 클릭 시 키보드 숨기기
        binding.activityCommunityCommentEdit.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
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

        // 뒤로 가기 버튼
        binding.commentEditBackIcon.setOnClickListener {
            finish()
        }

        // 완료 버튼
        binding.commentEditDone.setOnClickListener {
            updateComment(postId, commentId)
            finish()
        }
    }

    // 댓글 수정 시 기존 데이터 로드 함수
    private fun loadCommentData(commentId: Int) {
        CommunityRepository.getListComment(accessToken!!, postId) { response ->
            if (response != null) {
                val commentModel = findCommentById(response, commentId)

                if (commentModel != null) {
                    // 기존 데이터 UI에 반영
                    binding.commentEditText.setText(commentModel.content)

//                    // 이미지 데이터 처리
//                    commentModel.images.forEach { imageUri ->
//                        addImageToList(imageUri)
//                    }
                } else {
                    Log.e("상세댓글조회", "해당 댓글을 찾을 수 없습니다. commentId: $commentModel")
                }
            } else {
                Log.e("상세댓글조회", "해당 댓글 데이터가 없습니다.")
            }
        }
    }
    private fun findCommentById(comments: List<CommentModel>, commentId: Int): CommentModel? {
        for (comment in comments) {
            if (comment.id == commentId) {
                return comment
            }

            // 자식 댓글들을 재귀적으로 검색
            val foundInReplies = findCommentById(comment.replies!!, commentId)
            if (foundInReplies != null) {
                return foundInReplies
            }
        }
        return null
    }

    // 게시글 수정 함수
    private fun updateComment(postId: Int, commentId: Int) {

        val updateCommentRequest = UpdateCommentRequest(
            content = binding.commentEditText.text.toString(),
            images = emptyList(),
            secret = isSecret,
        )

        Log.d("수정댓글정보", "${postId}, ${commentId}, ${isSecret}")

        CommunityRepository.updateComment(accessToken!!, postId, commentId, updateCommentRequest) { response ->
            if (response != null) {
                Log.d("댓글수정", "댓글 수정 성공: ${response.content}")
            } else {
                Log.e("댓글수정", "댓글 수정 실패: ${response}")
            }
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
}