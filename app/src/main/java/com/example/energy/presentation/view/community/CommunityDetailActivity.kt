package com.example.energy.presentation.view.community

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.Comment
import com.example.energy.databinding.ActivityCommunityDetailBinding

class CommunityDetailActivity : AppCompatActivity(), ItemCommentAdapter.OnItemClickListener{
    private lateinit var binding: ActivityCommunityDetailBinding
    private lateinit var communityDB: CommunityPostDatabase
    private lateinit var dataList: ArrayList<Comment>
    private lateinit var commentAdapter: ItemCommentAdapter

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
                    // 이미지 로딩 등 추가 작업 수행
                }
            }.start()
        }

        // 뒤로가기 버튼
        binding.communityDetailBackIcon.setOnClickListener {
            finish() //현재 Activity 종료
        }
    }

    override fun addSubComment(comment: Comment) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("대댓글 달기")

        // Set up the input
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("확인") { dialog, which ->
            val subCommentText = input.text.toString()
            if (subCommentText.isNotEmpty()) {
                val subComment = Comment(
                    commentId = dataList.size + 1, // 임시 ID, 실제로는 DB에서 생성된 ID 사용
                    userInfo = "사용자 정보", // 실제 사용자 정보로 변경
                    body = subCommentText,
                    likeCount = 0,
                    parentCommentId = comment.commentId,
                    userLike = false,
                    createTime = "현재 시간", // 실제로는 현재 시간을 사용
                    updateTime = "현재 시간" // 실제로는 현재 시간을 사용
                )
                dataList.add(subComment)
                commentAdapter.itemSet = makeChildComment(dataList)
                commentAdapter.notifyDataSetChanged()
            }
        }
        builder.setNegativeButton("취소") { dialog, which -> dialog.cancel() }

        builder.show()
    }
}