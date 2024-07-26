package com.example.energy.presentation.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.energy.R
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.ActivityMainBinding
import com.example.energy.presentation.view.community.CommunityFragment
import com.example.energy.presentation.view.list.ListFragment
import com.example.energy.presentation.view.map.MapFragment
import com.example.energy.presentation.view.mypage.MypageFragment
import com.example.energy.presentation.view.note.NoteFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initBottomNavigation()
        inputDummyCommunity()

    }


    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, ListFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.listFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ListFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mapFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MapFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.communityFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, CommunityFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.noteFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, NoteFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    // 커뮤니티 피드 더미데이터 생성 함수
    private fun inputDummyCommunity(){
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val communityDB = CommunityPostDatabase.getInstance(this@MainActivity)!!
                val postInfo = communityDB.communityPostDao().getAllPosts()

                if(postInfo!!.isNotEmpty()) return@withContext

                communityDB.communityPostDao().insertPost(
                    CommunityPost(1, R.drawable.userimage, "김규리", "연희동 급 SOS",
                        "혹시 지금 연희동 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                        "도와줘요", R.drawable.tag_help, emptyList(), "1", "3"
                    )
                )
                communityDB.communityPostDao().insertPost(
                    CommunityPost(2, R.drawable.userimage, "정서현", "평창동 급 SOS",
                        "혹시 지금 평창동 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                        "궁금해요", R.drawable.tag_curious, emptyList(), "2", "0"
                    )
                )
                communityDB.communityPostDao().insertPost(
                    CommunityPost(3, R.drawable.userimage, "김수현", "서대문구 급 SOS",
                        "혹시 지금 서대문구 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                        "휠체어", R.drawable.tag_wheelchair, emptyList(), "0", "4"
                    )
                )
                communityDB.communityPostDao().insertPost(
                    CommunityPost(4, R.drawable.userimage, "주아연", "신촌 급 SOS",
                        "혹시 지금 신촌 쪽으로 도움 주러 오실 수 있는 분 계신가요? 멈춰서 움직일수가 없어요ㅠㅠ",
                        "스쿠터", R.drawable.tag_scooter, emptyList(), "5", "3"
                    )
                )
            }
        }
    }

}