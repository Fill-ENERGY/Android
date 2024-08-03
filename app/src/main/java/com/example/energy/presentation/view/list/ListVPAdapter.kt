package com.example.energy.presentation.view.list

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter




class ListVPAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity)
    {

    private val tabCount =3


    override fun getItemCount(): Int {
        return tabCount
    }


    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->   ListInfoFragment()
            1 ->   ListReviewFragment()
            2 ->   ListComplaintFragment()

            else -> throw IllegalStateException("Invalid position $position")


        }





    }


}