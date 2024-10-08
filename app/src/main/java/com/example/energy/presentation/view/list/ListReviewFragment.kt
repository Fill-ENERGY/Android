package com.example.energy.presentation.view.list

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.data.repository.review.ReviewModel
import com.example.energy.data.repository.review.ReviewRepository
import com.example.energy.databinding.FragmentListReviewBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.viewmodel.ListViewModel

class ListReviewFragment :
    BaseFragment<FragmentListReviewBinding>({ FragmentListReviewBinding.inflate(it) }) {
    val listViewModel by activityViewModels<ListViewModel>()

    private val reviewList = ArrayList<ReviewModel>()
    private val reviewAdapter = ListReviewAdapter(reviewList)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //토큰 가져오기
        var sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = sharedPreferences?.getString("accessToken", "none")

        //리사이클러뷰
        setRecyclerView()

        //추천순 디폴트
        sortReview(accessToken!!, "SCORE")

        //평가하기 버튼 클릭
        binding.tvGoElevation.setOnClickListener {
            val intent = Intent(activity, ListAddReviewActivity::class.java).apply {

                //activity에 데이터 전달
                listViewModel.getStationId.observe(viewLifecycleOwner, Observer { id ->
                    putExtra("stationId", id)
                })
                listViewModel.getStationName.observe(viewLifecycleOwner, Observer { name ->
                    putExtra("stationName", name)
                })
            }
            startActivity(intent)
        }

        //추천순으로 정렬
        binding.sortReviewLikeTv.setOnClickListener {
            sortReview(accessToken!!, "SCORE")
            binding.sortReviewLikeTv.setTextColor(Color.parseColor("#222019"))
            binding.sortReviewLatestTv.setTextColor(Color.parseColor("#71716E"))
        }

        //최신순으로 정렬
        binding.sortReviewLatestTv.setOnClickListener {
            sortReview(accessToken!!,"RECENT")
            binding.sortReviewLatestTv.setTextColor(Color.parseColor("#222019"))
            binding.sortReviewLikeTv.setTextColor(Color.parseColor("#71716E"))
        }

        //리사이클러뷰 클릭 이벤트
        reviewAdapter.setItemClickListener(object : ListReviewAdapter.OnItemClickListener {
            //리뷰 추천
            override fun onRecommendReview(reviewId: Int) {
                ReviewRepository.recommendReview(
                    accessToken!!, reviewId
                )
            }
        })
    }

    private fun setRecyclerView() {
        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvReview.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun sortReview(accessToken: String,sort: String) {
        listViewModel.getStationId.observe(viewLifecycleOwner, Observer { stationId ->
            ReviewRepository.getReviewsStation(
                accessToken,
                stationId, 0, sort, 10
            ) { response ->
                Log.d("테스틍", stationId.toString())
                binding.tvReviewTotalCount.text = (response?.size ?: 0).toString()
                if (response != null) {
                    reviewList.clear()
                    reviewList.addAll(response)
                    reviewAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}