package com.example.energy.presentation.view.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter




class ListVPAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)
    {

    private val tabCount =3



    private var stationId: Int = -1
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0



    override fun getItemCount(): Int {
        return tabCount
    }


    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->   ListInfoFragment().apply {

                arguments = Bundle().apply {
                    putInt("stationId", stationId)
                    putDouble("latitude", latitude)
                    putDouble("longitude", longitude)
                }

            }
            1 ->   ListReviewFragment()
            2 ->   ListComplaintFragment()

            else -> throw IllegalStateException("Invalid position $position")


        }





    }


        // 데이터를 업데이트하는 메서드
        fun updateData(stationId: Int, latitude: Double, longitude: Double) {
            this.stationId = stationId
            this.latitude = latitude
            this.longitude = longitude

            notifyDataSetChanged()
        }


}