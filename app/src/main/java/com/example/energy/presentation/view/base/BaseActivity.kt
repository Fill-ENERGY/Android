package com.example.energy.presentation.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding>(private val inflater: (LayoutInflater) -> T) : AppCompatActivity() {
    protected lateinit var binding: T
    private var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflater(layoutInflater)
        setContentView(binding.root)
    }

    fun showToast(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 키보드 숨기기
    fun hideKeyboard(v: View){
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}