package com.example.energy.presentation.view.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.energy.databinding.DialogHelpStatusBinding

class DialogHelpStatus : AppCompatActivity(){
    lateinit var binding: DialogHelpStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogHelpStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}