package com.example.energy.presentation.view.community

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.energy.R
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.FragmentCommunityWholeBinding
import com.example.energy.presentation.view.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommunityWholeFragment : BaseFragment<FragmentCommunityWholeBinding>({ FragmentCommunityWholeBinding.inflate(it)}) {

    var postInfo = ArrayList<CommunityPost>() //커뮤니티 데이터 리스트
    val categoriesList = "도와줘요" //임시 카테고리 리스트
    val imageUrlsList: List<Uri> = emptyList() // 임시 이미지 리스트
    private lateinit var communityDB: CommunityPostDatabase
    private lateinit var postCommunityAdapter: PostCommunityRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // community_post에 데이터 리스트 생성
//        communityDB = CommunityPostDatabase.getInstance(requireContext())!!
//        postInfo.addAll(communityDB.communityPostDao().getAllPosts())
//
//        // RecyclerView 초기화 및 데이터 연결
//        val postCommunityAdapter = PostCommunityRVAdapter(postInfo)
//        binding.wholeCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        binding.wholeCommunityPostRv.adapter = postCommunityAdapter

        // community_post에 데이터 리스트 생성
        communityDB = CommunityPostDatabase.getInstance(requireContext())!!

        // 백그라운드 스레드에서 데이터베이스 접근
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                postInfo.addAll(communityDB.communityPostDao().getAllPosts())
            }
            // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
            postCommunityAdapter = PostCommunityRVAdapter(postInfo)
            binding.wholeCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.wholeCommunityPostRv.adapter = postCommunityAdapter
        }
    }

    // 게시글 리스트 업데이트 메서드
    fun updatePostList(newPost: CommunityPost) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                postInfo.add(newPost)
            }
            postCommunityAdapter.notifyItemInserted(postInfo.size + 1)
            binding.wholeCommunityPostRv.scrollToPosition(postInfo.size + 1)
        }
    }
}