package com.example.energy.presentation.view.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.energy.databinding.ActivityCommunityCommentEditBinding

class CommentEditActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCommunityCommentEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityCommentEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 뒤로 가기 버튼
        binding.commentEditBackIcon.setOnClickListener {
            finish()
        }

        // 완료 버튼
        binding.commentEditDone.setOnClickListener {
            finish()
        }
    }
}