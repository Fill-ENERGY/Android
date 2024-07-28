package com.example.energy.presentation.util

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.energy.databinding.DialogCustomBinding

class EnergyUtils {
    companion object {
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
    }
}

