package com.example.energy.presentation.view.community

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.example.energy.databinding.ItemSelectCategoryBinding

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
        return binding.root
    }

    // 드롭다운된 항목들 리스트의 뷰
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSelectCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.itemCategoryTitle.text = menuList[position]

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
