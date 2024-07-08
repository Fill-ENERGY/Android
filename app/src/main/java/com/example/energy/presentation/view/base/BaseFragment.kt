package com.example.energy.presentation.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>(private val inflater: (LayoutInflater) -> T) : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!
    private var imm : InputMethodManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflater(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 키보드 숨기기
    fun hideKeyboard(v: View){
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}