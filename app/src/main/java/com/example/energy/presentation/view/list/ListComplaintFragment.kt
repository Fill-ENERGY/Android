package com.example.energy.presentation.view.list

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import com.example.energy.databinding.FragmentListComplaintBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListComplaintFragment : BaseFragment<FragmentListComplaintBinding>({ FragmentListComplaintBinding.inflate(it) }) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //관리기관 정보 복사
        copyToClipboard()

        //민원 글 작성하기 페이지로 이동
        binding.btnWriteComplaint.setOnClickListener {
            //startActivity(Intent(activity, CommunityWritingActivity::class.java))
        }
    }

        //관리 기관 정보 복사
        private fun copyToClipboard() {
        val clipboard: ClipboardManager =
            requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        binding.tvManageCopy.setOnClickListener {
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "label",
                    binding.tvManageInstitution.text.toString()
                )
            )
        }

        binding.tvNumberCopy.setOnClickListener {
            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "label",
                    binding.tvNumberInstitution.text.toString()
                )
            )
        }
    }


}