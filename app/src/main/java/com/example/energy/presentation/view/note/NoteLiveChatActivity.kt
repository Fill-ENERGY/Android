package com.example.energy.presentation.view.note

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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


        // 프로필로 전환하는 클릭 리스너
        val clickListener = View.OnClickListener {
            val intent = Intent(this, NoteUserProfileActivity::class.java).apply {
                putExtra("Username", username)
                putExtra("Id", userId)
            }
            startActivity(intent)
        }


        //프로필 전환 클릭 리스너 적용
        binding.usernameTextView.setOnClickListener(clickListener)
        binding.userIdTextView.setOnClickListener(clickListener)
        binding.UserProfile.setOnClickListener(clickListener)
        binding.showMoreButton.setOnClickListener(clickListener)




    }
}