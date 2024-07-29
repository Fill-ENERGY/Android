package com.example.energy.presentation.view.community

import android.os.Bundle
import android.os.Message
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.Comment
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.ActivityCommunityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommunityDetailActivity : AppCompatActivity(), ItemCommentAdapter.OnItemClickListener{
    private lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var communityDB: CommunityPostDatabase
    private lateinit var dataList: ArrayList<Comment>
    private lateinit var commentAdapter: ItemCommentAdapter
    private var parentCommentId: Int = -1 // 자식 댓글의 부모 댓글 ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize dataList
        dataList = ArrayList()

        // 댓글 RecyclerView 연결
        commentAdapter = ItemCommentAdapter(this, dataList)
        commentAdapter.onItemClickListener = this
        binding.communityDetailCommentView.adapter = commentAdapter
        binding.communityDetailCommentView.layoutManager = LinearLayoutManager(this)

//        // Room 데이터베이스 인스턴스 생성
//        db = Room.databaseBuilder(
//            applicationContext,
//            CommunityPostDatabase::class.java, "database-name"
//        ).allowMainThreadQueries().build()

        // 인텐트로부터 전달받은 postId 가져옴. 기본값은 -1로 설정하여 예외처리
        val postId = intent.getIntExtra("postId", -1)

        // 전달받은 데이터 수신
        if (postId != -1) {
            Thread {
                communityDB = CommunityPostDatabase.getInstance(this@CommunityDetailActivity)!!
                val postInfo = communityDB.communityPostDao().getPostById(postId)
                runOnUiThread {
                    // CommunityPost 객체의 데이터를 UI 요소에 반영
                    binding.communityDetailUserProfile.setImageResource(postInfo.userProfile!!)
                    binding.communityDetailUserName.text = postInfo.userName
                    binding.communityDetailTitle.text = postInfo.title
                    binding.communityDetailContent.text = postInfo.content
                    binding.communityDetailLikeNum.text = postInfo.likes
                    binding.communityDetailCommentNum.text = postInfo.comments
                    binding.communityDetailCategoryTitle.text = postInfo.categoryString
                    Log.d("imageUrl2", postInfo.imageUrl.toString())

                    // 이미지 RecyclerView 설정
                    if(postInfo.imageUrl.isEmpty()){
                        // 이미지가 없는 경우 RecyclerView 숨기기
                        binding.communityDetailImage.visibility = View.GONE
                    } else{
                        binding.communityDetailImage.visibility = View.VISIBLE
                        // RecyclerView의 LayoutManager 및 Adapter 설정
                        val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                        binding.communityDetailImage.layoutManager = layoutManager

                        val imageAdapter = ItemFeedPhotoAdapter(postInfo.imageUrl)
                        Log.d("imageUrl", postInfo.imageUrl.toString())
                        binding.communityDetailImage.adapter = imageAdapter
                    }
                }
            }.start()
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

        // 댓글 전송 버튼
        binding.sendButton.setOnClickListener {
            val commentText = binding.messageInput.text.toString()
            if (commentText.isNotBlank()) {
                val newComment = Comment(
                    commentId = dataList.size + 1, // 임시 ID, 실제로는 DB에서 생성된 ID 사용
                    userInfo = "사용자 정보", // 실제 사용자 정보로 변경
                    body = commentText,
                    parentCommentId = parentCommentId, // 자식 댓글의 부모 ID
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                )
                // 자식 댓글인 경우 부모 댓글에 추가
                if (parentCommentId != -1) {
                    val parentComment = dataList.find { it.commentId == parentCommentId }
                    parentComment?.let {
                        dataList.add(newComment)
                        commentAdapter.itemSet = makeChildComment(dataList)
                        commentAdapter.notifyDataSetChanged()
                        parentCommentId = -1 // 부모 ID 초기화
                    }
                } else {
                    // 부모 댓글일 경우 데이터 리스트에 추가
                    dataList.add(newComment)
                    commentAdapter.itemSet = makeChildComment(dataList)
                    commentAdapter.notifyDataSetChanged()
                }

                // 데이터베이스에 저장
//                CoroutineScope(Dispatchers.IO).launch {
//                    communityDB.commentDao().insertComment(newComment)
//                    runOnUiThread {
//                        updateCommentsList(newComment)
//                    }
//                }
                addCommentToDatabase(newComment)

                binding.messageInput.text.clear()

            }
        }
    }

    // 댓글 추가 기능
    private fun addCommentToDatabase(comment: Comment) {
        Thread {
            communityDB.commentDao().insertComment(comment)
            runOnUiThread {
                updateCommentsList(comment)
            }
        }.start()
    }

    // 대댓글 추가 기능
    override fun addSubComment(comment: Comment) {
        // 부모 댓글 ID를 설정하여 자식 댓글을 작성할 준비
        parentCommentId = comment.commentId
    }

    // Comment 업데이트 함수
    private fun updateCommentsList(newComment: Comment) {
//        val fragmentManager: FragmentManager = supportFragmentManager
//        val communityWholeFragment = fragmentManager.findFragmentByTag("CommunityWholeFragment") as CommunityWholeFragment?
//        communityWholeFragment?.updatePostList(newComment)
        dataList.add(newComment)
        commentAdapter.itemSet = makeChildComment(dataList)
        commentAdapter.notifyDataSetChanged()
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