package com.example.energy.presentation.view.community

import android.os.Bundle
import android.os.Message
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.Comment
import com.example.energy.databinding.ActivityCommunityDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommunityDetailActivity : AppCompatActivity(), ItemCommentAdapter.OnItemClickListener{
    private lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var communityDB: CommunityPostDatabase
    private lateinit var dataList: ArrayList<Comment>
    private lateinit var commentAdapter: ItemCommentAdapter
    var itemSet = makeChildComment(dataList) //자식 정렬 알고리즘 호출
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

        // 뒤로가기 버튼
        binding.communityDetailBackIcon.setOnClickListener {
            finish() //현재 Activity 종료
        }

        // 댓글 전송 버튼
        binding.sendButton.setOnClickListener {
            val commentText = binding.messageInput.text.toString()
            if (commentText.isNotBlank()) {
                val newComment = Comment(
                    id = dataList.size + 1, // 임시 ID, 실제로는 DB에서 생성된 ID 사용
                    userInfo = "사용자 정보", // 실제 사용자 정보로 변경
                    body = commentText,
                    parentCommentId = parentCommentId, // 자식 댓글의 부모 ID
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                )
                // 자식 댓글인 경우 부모 댓글에 추가
                if (parentCommentId != -1) {
                    val parentComment = dataList.find { it.id == parentCommentId }
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
                binding.messageInput.text.clear()
            }
        }
    }

//    // 댓글 추가 기능
//    private fun addCommentToDatabase(commentText: String) {
//        // 댓글 객체 생성
//        val newComment = Comment(
//            commentId = dataList.size + 1, // 임시 ID, 실제로는 DB에서 생성된 ID 사용
//            userInfo = "사용자 정보", // 실제 사용자 정보로 변경
//            body = commentText,
//            parentCommentId = null, // 대댓글이 아니라면 null 또는 실제 부모 ID
//            createTime = Date().toString(), // 현재 시간으로 변경
//        )
//
//        // 댓글을 데이터베이스에 저장 (Thread로 실행)
//        Thread {
//            communityDB.commentDao().insertComment(newComment)
//            val updatedComments = communityDB.commentDao().getAllComments()
//            runOnUiThread {
//                dataList.clear()
//                dataList.addAll(makeChildComment(ArrayList(updatedComments)))
//                commentAdapter.notifyDataSetChanged()
//                binding.messageInput.text.clear() // 입력 필드 비우기
//            }
//        }.start()
//    }

    // 대댓글 추가 기능
    override fun addSubComment(comment: Comment) {
        // 부모 댓글 ID를 설정하여 자식 댓글을 작성할 준비
        parentCommentId = comment.id
    }
}