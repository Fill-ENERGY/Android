package com.example.energy.presentation.view.list

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energy.R

class ListInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_information)


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
    }
}