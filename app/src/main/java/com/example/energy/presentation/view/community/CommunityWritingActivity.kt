package com.example.energy.presentation.view.community

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.energy.R
import com.example.energy.databinding.ActivityCommunityWritingBinding

class CommunityWritingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommunityWritingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //뒤로가기 버튼
        binding.communityWritingBackIcon.setOnClickListener {
            finish() // 현재 액티비티 종료
        }
    }

}