package com.example.energy.presentation.view.community

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.databinding.FragmentCommunityNotifiWithDataBinding
import com.example.energy.data.repository.community.Notification
import com.example.energy.presentation.view.base.BaseFragment

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

        // 리사이클러뷰에 스와이프 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(notificationRVAdapter).apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 5)    // 1080 / 5 = 216
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notificationRecyclerView)

        // 다른 곳 터치 시 기존 선택했던 뷰 닫기
        binding.notificationRecyclerView.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding.notificationRecyclerView)
            false
        }
    }
}