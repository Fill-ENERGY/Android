package com.example.energy.presentation.view.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.energy.R
import com.example.energy.databinding.FragmentMypageBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

class MypageFragment : BaseFragment<FragmentMypageBinding>({ FragmentMypageBinding.inflate(it)}) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar()

        setUserInfo()

        binding.btnLogout.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    showToast("로그아웃에 실패했습니다.")
                }
                else {
                    Log.i("로그아웃", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    //남아있는 스택 지우며 이동
                    activity?.finishAffinity()
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }


    }

    private fun setToolBar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu_mypage)
        binding.toolbar.setTitle(R.string.mypage)
        binding.toolbar.setTitleTextAppearance(requireContext(), R.style.Title1)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.gray_scale8))
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.appbar_notification -> {
                    showToast("notification")
                    true
                }

                R.id.appbar_sos -> {
                    showToast("sos")
                    startActivity(Intent(activity, BlockActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun setUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("유저정보", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    "유저정보", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )

                binding.tvNickname.text = user.kakaoAccount?.profile?.nickname
                Glide.with(this)
                    .load(user.kakaoAccount?.profile?.thumbnailImageUrl)
                    .into(binding.ivProfile)
            }
        }
    }
}