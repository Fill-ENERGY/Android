package com.example.energy.presentation.view.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.energy.R
import com.example.energy.data.repository.auth.AuthRepository
import com.example.energy.data.repository.block.BlockRepository
import com.example.energy.data.repository.map.MapRepository
import com.example.energy.data.repository.profile.ProfileRepository
import com.example.energy.databinding.FragmentMypageBinding
import com.example.energy.presentation.util.EnergyUtils
import com.example.energy.presentation.view.MainActivity
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.community.NotificationActivity
import com.example.energy.presentation.view.login.LoginActivity
import com.kakao.sdk.user.UserApiClient

class MypageFragment : BaseFragment<FragmentMypageBinding>({ FragmentMypageBinding.inflate(it)}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = sharedPreferences?.getString("accessToken", "none")
        //var refreshToken = sharedPreferences?.getString("refreshToken", "none")

        //프로필 테스트
        ProfileRepository.getMyProfile(accessToken!!) {
            myProfile ->
            if(myProfile != null) {
                // 로그인이 된 상태라면
                // 로그인 버튼 비활성화
                binding.btnNeedLogin.visibility = View.GONE
                // 프로필 정보 버튼 활성화
                binding.tvNickname.visibility = View.VISIBLE
                binding.tvProfileEdit.visibility = View.VISIBLE
                binding.ivProfileViewMore.visibility = View.VISIBLE
                // 로그아웃 버튼 활성화
                binding.divide3.visibility = View.VISIBLE
                binding.btnLogout.visibility = View.VISIBLE

                //내 정보 설정
                binding.tvNickname.text = myProfile.name
            } else {
                //로그아웃 된 상태라면
                // 로그인 버튼 활성화
                binding.btnNeedLogin.visibility = View.VISIBLE
                // 프로필 정보 버튼 비활성화
                binding.tvNickname.visibility = View.GONE
                binding.tvProfileEdit.visibility = View.GONE
                binding.ivProfileViewMore.visibility = View.GONE
            }
        }

        //즐겨찾기 수 띄우기
        MapRepository.getBookmarkStation(accessToken, "DISTANCE", 0, 10, 35.5, 35.5) {
            response ->
            if (response != null) {
                binding.tvBookmark.text = response.size.toString()
            }
        }

        setToolBar()

        setUserInfo()

        binding.btnBlocks.setOnClickListener {
            startActivity(Intent(activity, BlockActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    showToast("로그아웃에 실패했습니다.")
                }
                else {
                    Log.i("로그아웃", "로그아웃 성공. SDK에서 토큰 삭제됨")
                    //남아있는 스택 지우며 이동
                    showToast("로그아웃에 성공했습니다.")

                    //토큰 삭제
                    val sharedPreferences = requireActivity().getSharedPreferences("userToken",
                        AppCompatActivity.MODE_PRIVATE
                    )
                    val editor = sharedPreferences.edit()

                    editor.putString("accessToken", "")
                    editor.putString("refreshToken", "")
                    editor.apply()

                    activity?.finishAffinity()
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }


        binding.btnNeedLogin.setOnClickListener {
            //로그인
            startActivity(Intent(activity, LoginActivity::class.java))
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
                    startActivity(Intent(activity, NotificationActivity::class.java))
                    true
                }

                R.id.appbar_sos -> {
                    EnergyUtils.showSOSDialog(requireContext())
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