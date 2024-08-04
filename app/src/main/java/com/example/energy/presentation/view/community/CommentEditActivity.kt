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
    }
}