package com.example.energy.presentation.view.community

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.databinding.FragmentCommunityScooterBinding
import com.example.energy.presentation.view.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommunityScooterFragment : BaseFragment<FragmentCommunityScooterBinding>({ FragmentCommunityScooterBinding.inflate(it)}) {

    var postInfo = ArrayList<CommunityPost>() //커뮤니티 데이터 리스트
    private lateinit var communityDB: CommunityPostDatabase
    private lateinit var postCommunityAdapter: PostCommunityRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = sharedPreferences?.getString("accessToken", "none")

        //test
        CommunityRepository.getListCommunity(accessToken!!, "SCOOTER", 0, 10, "") {
                response ->
            response.let {
                //통신성공
                if (response != null && response.board != null) {
                    // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
                    postCommunityAdapter = PostCommunityRVAdapter(response.board)
                    binding.scooterCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    binding.scooterCommunityPostRv.adapter = postCommunityAdapter
                } else {
                    Log.e("전체커뮤니티api테스트", "응답 결과가 null이거나 board가 없습니다.")
                }
            }
        }

//        // community_post에 데이터 리스트 생성
//        communityDB = CommunityPostDatabase.getInstance(requireContext())!!
//
//        // 백그라운드 스레드에서 데이터베이스 접근
//        lifecycleScope.launch {
//            withContext(Dispatchers.IO) {
//                val allPosts = communityDB.communityPostDao().getAllPosts()
//                postInfo.addAll(allPosts.filter { it.categoryString == "스쿠터" })
//            }
//            // RecyclerView 초기화 및 데이터 연결 (메인 스레드)
//            postCommunityAdapter = PostCommunityRVAdapter(postInfo)
//            binding.scooterCommunityPostRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            binding.scooterCommunityPostRv.adapter = postCommunityAdapter
//        }
    }
}