package com.example.energy.presentation.view.community

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.energy.R
import com.example.energy.databinding.ActivityCommunityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 데이터를 확인하는 로직 (예시로 SharedPreferences 사용)
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val hasData = sharedPreferences.getBoolean("hasData", false)

        // Fragment 전환 로직
        if (true) {
            replaceFragment(NotifyFragmentWithData())
        } else {
            replaceFragment(NotifyFragmentNoData())
        }

        // 뒤로가기 버튼
        binding.notificationBackIcon.setOnClickListener {
            finish()
        }
    }

    private fun replaceFragment(fragment: Fragment) { // fragment 전환 함수
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.notification_fragment, fragment)
        fragmentTransaction.commit()
    }
}