package com.example.energy.presentation.view.community

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.FragmentCommunityBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.community.CommunityWritingActivity
import com.google.android.material.tabs.TabLayoutMediator

class CommunityFragment : BaseFragment<FragmentCommunityBinding>({ FragmentCommunityBinding.inflate(it)}) {

    private val information = arrayListOf("전체보기", "일상", "궁금해요", "도와줘요", "휠체어", "스쿠터")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // community viewpager adapter 연결 및 초기화
        val communityAdapter = CommunityVPAdapter(this)
        binding.communityViewpage.adapter = communityAdapter
        TabLayoutMediator(binding.communityCategoryTabLayout, binding.communityViewpage){ // tab item과 viewpage fragment 연결
                tab, position ->
            tab.text = information[position]
        }.attach()

        // 글 쓰기 화면으로 전환
        binding.communityWritingBt.setOnClickListener {
            startActivity(Intent(activity, CommunityWritingActivity::class.java))
        }

        // 알림 아이콘 선택시
        binding.communityNotifyIv.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            startActivity(intent)
        }

        // SOS 버튼
        binding.communitySosIv.setOnClickListener {
            showSOSDialog()
        }
    }

    private fun showSOSDialog() {
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val window = dialog.window
            val layoutParams = window?.attributes

            // 디바이스 너비의 70%로 설정
            val width = (resources.displayMetrics.widthPixels * 0.7).toInt()

            //radius 적용
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            layoutParams?.width = width
            window?.attributes = layoutParams
        }
        dialog.show()

        dialogBinding.btnDialog.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")

            startActivity(intent)
            dialog.dismiss()
        }

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }
}