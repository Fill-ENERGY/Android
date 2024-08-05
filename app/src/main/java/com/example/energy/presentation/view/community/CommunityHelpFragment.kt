package com.example.energy.presentation.view.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.FragmentCommunityHelpBinding
import com.example.energy.presentation.view.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommunityHelpFragment : BaseFragment<FragmentCommunityHelpBinding>({ FragmentCommunityHelpBinding.inflate(it)}) {

    var postInfo = ArrayList<CommunityPost>() //커뮤니티 데이터 리스트
    private lateinit var communityDB: CommunityPostDatabase
    private lateinit var postCommunityAdapter: PostCommunityRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // community_post에 데이터 리스트 생성
        communityDB = CommunityPostDatabase.getInstance(requireContext())!!

        // 백그라운드 스레드에서 데이터베이스 접근
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val allPosts = communityDB.communityPostDao().getAllPosts()
                postInfo.addAll(allPosts.filter { it.categoryString == "도와줘요" })
            }
            // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
            postCommunityAdapter = PostCommunityRVAdapter(postInfo)
            binding.helpCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.helpCommunityPostRv.adapter = postCommunityAdapter
        }
    }
}