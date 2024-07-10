package com.example.energy.presentation.view.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.energy.R
import com.example.energy.databinding.FragmentMypageBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

class MypageFragment : BaseFragment<FragmentMypageBinding>({ FragmentMypageBinding.inflate(it)}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogout.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    showToast("로그아웃에 실패했습니다.")
                }
                else {
                    Log.i("로그아웃", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }


    }
}