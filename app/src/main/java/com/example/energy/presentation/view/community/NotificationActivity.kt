package com.example.energy.presentation.view.community

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.energy.R

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_notification)

        // 상단 바 초기화
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "알림"

        // 데이터를 확인하는 로직 (예시로 SharedPreferences 사용)
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val hasData = sharedPreferences.getBoolean("hasData", false)

        // Fragment 전환 로직
        if (true) {
            replaceFragment(NotifyFragmentWithData())
        } else {
            replaceFragment(NotifyFragmentNoData())
        }
    }

    private fun replaceFragment(fragment: Fragment) { // fragment 전환 함수
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.notification_fragment, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 각 Toolbar의 아이템 별 반응
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_edit -> {
                // 편집 버튼 클릭 시 동작
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //Toolbar 초기화
        menuInflater.inflate(R.menu.community_notification_toolbar, menu)
        return true
    }
}