package com.example.energy.presentation.view.mypage

import android.os.Bundle
import android.view.MenuItem
import com.example.energy.R
import com.example.energy.databinding.ActivityBlockBinding
import com.example.energy.presentation.view.base.BaseActivity

class BlockActivity : BaseActivity<ActivityBlockBinding>({ ActivityBlockBinding.inflate(it)}) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

}