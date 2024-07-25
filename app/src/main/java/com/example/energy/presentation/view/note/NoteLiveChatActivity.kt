package com.example.energy.presentation.view.note

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energy.R
import com.example.energy.databinding.ActivityNoteLiveChatBinding

class NoteLiveChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteLiveChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // username, Id 가져오기

        val username = intent.getStringExtra("Username") ?: "김규리"
        val userId = intent.getStringExtra("Id") ?: "rlarbfl"


        binding.usernameTextView.text = username
        binding.userIdTextView.text = userId


        //뒤로 가기 버튼 클릭 시
        binding.backbutton.setOnClickListener {
            finish()
        }
    }
}