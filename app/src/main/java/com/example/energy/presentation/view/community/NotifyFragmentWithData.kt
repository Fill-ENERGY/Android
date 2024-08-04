package com.example.energy.presentation.view.community

import Swipe
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.community.Notification
import com.example.energy.databinding.FragmentCommunityNotifiWithDataBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.note.SwipeHelper

class NotifyFragmentWithData : BaseFragment<FragmentCommunityNotifiWithDataBinding>({ FragmentCommunityNotifiWithDataBinding.inflate(it)}){

    private var notifications: ArrayList<Notification> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 리스트 생성 (더미 데이터)
        notifications.apply {
            add(Notification(1, R.drawable.userimage, "김한주", true))
            add(Notification(2, R.drawable.userimage, "김한주", false))
        }

        // Adapter와 Datalist 연결
        val notificationRVAdapter = NotificationRVAdapter(notifications)
        binding.notificationRecyclerView.adapter = notificationRVAdapter
        binding.notificationRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //스와이프 삭제 리사이클러뷰와 연결
        val itemTouchHelper = ItemTouchHelper(Swipe(notificationRVAdapter))
        itemTouchHelper.attachToRecyclerView(binding.notificationRecyclerView)
    }
}