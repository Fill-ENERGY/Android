package com.example.energy.presentation.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.DialogLoginBinding
import com.example.energy.presentation.view.login.LoginActivity

class EnergyUtils {
    companion object {
        //sos 다이얼로그
        fun showSOSDialog(context: Context) {
            val dialogBinding = DialogCustomBinding.inflate(LayoutInflater.from(context))
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogBinding.root)

            val dialog = builder.create()
            dialog.setOnShowListener {
                val window = dialog.window
                val layoutParams = window?.attributes

                // 디바이스 너비의 70%로 설정
                val width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()

                //radius 적용
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                layoutParams?.width = width
                window?.attributes = layoutParams
            }
            dialog.show()

            dialogBinding.btnDialog.setOnClickListener {
                var intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:112")

                context.startActivity(intent)
                dialog.dismiss()
            }

            dialogBinding.ivClose.setOnClickListener {
                dialog.dismiss()
            }
        }

        //login 다이얼로그
        fun showLoginDialog(context: Context) {
            val dialogBinding = DialogLoginBinding.inflate(LayoutInflater.from(context))
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogBinding.root)

            val dialog = builder.create()
            dialog.setOnShowListener {
                val window = dialog.window
                val layoutParams = window?.attributes

                // 디바이스 너비의 70%로 설정
                val width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()

                //radius 적용
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                layoutParams?.width = width
                window?.attributes = layoutParams
            }
            dialog.show()

            //카카오 로그인
            dialogBinding.btnKakaoLogin.setOnClickListener {
                context.startActivity(Intent(context, LoginActivity::class.java))
                dialog.dismiss()
            }

            //직접 입력해서 로그인
            dialogBinding.tvLoginInput.setOnClickListener {
                context.startActivity(Intent(context, LoginActivity::class.java))
                dialog.dismiss()
            }

            dialogBinding.ivClose.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}

