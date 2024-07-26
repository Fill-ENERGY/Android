package com.example.energy.presentation.view.note

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energy.R
import com.example.energy.databinding.ActivityNoteUserProfileBinding

class NoteUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // username, Id 가져오기

        val username = intent.getStringExtra("Username") ?: "김규리"
        val userId = intent.getStringExtra("Id") ?: "rlarbfl"


        binding.usernameTextView.text = username
        binding.userIdTextView.text = userId

    }
}