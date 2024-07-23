package com.example.energy.presentation.view.list

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energy.R
import com.example.energy.databinding.ActivityListInformationBinding

class ListInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListInformationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityListInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //리사이클러뷰 아이템 데이터 받기


        val locationName = intent.getStringExtra("location_name")
        val distance = intent.getStringExtra("distance")
        val grade = intent.getStringExtra("grade")
        val time = intent.getStringExtra("time")


        val locationNameTextView: TextView = findViewById(R.id.LocationName)
        val distanceTextView: TextView = findViewById(R.id.Distance)
        val gradeTextView: TextView = findViewById(R.id.Grade)
        val timeTextView: TextView = findViewById(R.id.timetext)

        locationNameTextView.text = locationName
        distanceTextView.text = distance
        gradeTextView.text = grade
        timeTextView.text = time

        //뒤로 가기 버튼 클릭 시
        binding.backbutton.setOnClickListener {
            finish()
        }



    }



}