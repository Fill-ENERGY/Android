package com.example.energy.presentation.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.energy.R


class ListInformationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // findViewById를 사용하여 뷰 참조
        val locationNameTextView: TextView = view.findViewById(R.id.LocationName)
        val distanceTextView: TextView = view.findViewById(R.id.Distance)
        val gradeTextView: TextView = view.findViewById(R.id.Grade)
        val timeTextView: TextView = view.findViewById(R.id.timetext)

        arguments?.let {
            val locationName = it.getString("location_name")
            val distance = it.getString("distance")
            val grade = it.getString("grade")
            val time = it.getString("time")

            // UI에 데이터 바인딩
            locationNameTextView.text = locationName
            distanceTextView.text = distance
            gradeTextView.text = grade
            timeTextView.text = time
        }
    }
}
