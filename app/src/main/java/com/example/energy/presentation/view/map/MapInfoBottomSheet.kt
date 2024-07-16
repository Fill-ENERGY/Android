package com.example.energy.presentation.view.map

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import com.example.energy.databinding.BottomsheetMapBinding
import com.example.energy.databinding.DialogLoginBinding
import com.example.energy.presentation.view.community.CommunityWritingActivity
import com.example.energy.presentation.view.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapInfoBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: BottomsheetMapBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomsheetMapBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //전화하기
        callCharging("112")


        //즐겨찾기
        bookmarkCharging()

        //길찾기
        val bundle = arguments
        if (bundle != null && bundle.containsKey("location")) {
            val currentLocation = bundle.getParcelable<Location>("location")
            if (currentLocation != null) {
                searchCharging(currentLocation)
            }
        }

        //공유하기
        shareCharging("https://developer.android.com/training/sharing/send?hl=ko")

        //민원 탭
        showTabComplaint()
    }

    private fun showTabComplaint() {
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

        binding.btnWriteComplaint.setOnClickListener {
            startActivity(Intent(activity, CommunityWritingActivity::class.java))
        }
    }


    private fun bookmarkCharging() {
        binding.ivBookmark.setOnClickListener {
            //즐겨찾기 로직
            showLoginDialog()
        }
    }

    private fun searchCharging(currentLocation: Location) {
        binding.ivGuide.setOnClickListener {
            //카카오 지도로 연결
            val endLocation = Pair(37.49795, 127.027637)

            val url =
                "kakaomap://route?sp=${currentLocation.latitude},${currentLocation.longitude}&ep=${endLocation.first},${endLocation.second}&by=FOOT"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)

            // 카카오맵이 설치되어 있다면 앱으로 연결, 설치되어 있지 않다면 스토어로 이동
            if (isAppInstalled("net.daum.android.map", requireContext().packageManager)) {
                //카카오맵으로 이동
                startActivity(intent)
            } else {
                //스토어로 이동
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=net.daum.android.map")
                    )
                )

            }

        }
    }

    private fun callCharging(callNumber: String) {
        binding.ivCall.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${callNumber}")

            startActivity(intent)
        }
    }

    private fun shareCharging(chargingUrl: String) {
        binding.ivShare.setOnClickListener {
            //기본 셰어 창 연결
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
            intent.type = "text/plain"

            val content = "링크 공유하기\n확인해보세요!"
            intent.putExtra(Intent.EXTRA_TEXT, "$content\n\n$chargingUrl")

            val chooserTitle = "친구에게 공유하기"
            startActivity(Intent.createChooser(intent, chooserTitle))
        }
    }

    private fun isAppInstalled(packageName : String, packageManager : PackageManager) : Boolean{
        return try{
            packageManager.getPackageInfo(packageName, 0)
            true
        }catch (ex : PackageManager.NameNotFoundException){
            false
        }
    }

    private fun showLoginDialog() {
        val dialogBinding = DialogLoginBinding.inflate(layoutInflater)
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

        dialogBinding.btnKakaoLogin.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
            dialog.dismiss()
        }

        dialogBinding.tvLoginInput.setOnClickListener {
            //직접 입력하기 로직
            dialog.dismiss()
        }

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }
}