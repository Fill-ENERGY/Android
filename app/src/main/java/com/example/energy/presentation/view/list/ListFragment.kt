package com.example.energy.presentation.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.energy.R
import com.example.energy.databinding.FragmentCommunityBinding
import com.example.energy.databinding.FragmentListBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListFragment : BaseFragment<FragmentListBinding>({ FragmentListBinding.inflate(it)}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}