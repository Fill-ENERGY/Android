package com.example.energy.presentation.view.community

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.example.energy.R
import com.example.energy.databinding.ItemSelectCategoryBinding
import com.google.android.material.color.MaterialColors.getColor

class OptionSpinnerAdapter(
    context: Context, @LayoutRes private val resId: Int,
    private val menuList: List<String>,
    private val initText: String
) : ArrayAdapter<String>(context, resId, menuList) {

    private var selectedPosition = -1 // 선택된 항목의 위치를 저장할 변수

    // 드롭다운하지 않은 상태의 Spinner 항목의 뷰
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSelectCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.itemCategoryCheckbox.visibility = View.GONE
        binding.itemCategoryUncheckbox.visibility = View.GONE
        binding.itemCategoryTitle.text = if (selectedPosition == -1) initText else menuList[selectedPosition]
        if (selectedPosition == -1) {
            // 아무것도 선택되지 않은 상태
            binding.itemCategoryTitle.setTextAppearance(R.style.Title3)
            binding.itemCategoryTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_scale6))
        } else {
            // 선택된 아이템 상태
            binding.itemCategoryTitle.setTextAppearance(R.style.Title2)
            binding.itemCategoryTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_scale8))
        }
        return binding.root
    }

    // 드롭다운된 항목들 리스트의 뷰
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSelectCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.itemCategoryTitle.text = menuList[position]

        binding.itemCategoryTitle.setTextAppearance(R.style.Title3) // 드롭다운 아이템 스타일
        binding.itemCategoryTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_scale8)) // 드롭다운 아이템 색상

        if (position == selectedPosition) { //item이 선택되었을 때
            binding.itemCategoryCheckbox.visibility = View.VISIBLE
            binding.itemCategoryUncheckbox.visibility = View.GONE
        } else {
            binding.itemCategoryCheckbox.visibility = View.GONE
            binding.itemCategoryUncheckbox.visibility = View.VISIBLE
        }

        binding.itemCategoryTitle.gravity = Gravity.END

        return binding.root
    }

    override fun getCount() = menuList.size

    override fun getItem(position: Int) = menuList[position]

    override fun getItemId(position: Int) = position.toLong()

    // 선택된 항목 설정 메소드
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // 변경사항 적용을 위해 데이터셋을 새로고침
    }
}
