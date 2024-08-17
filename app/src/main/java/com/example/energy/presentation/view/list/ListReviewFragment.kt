package com.example.energy.presentation.view.list

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.review.ReviewModel
import com.example.energy.data.repository.review.ReviewRepository
import com.example.energy.databinding.FragmentListReviewBinding
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.view.base.BaseFragment
import okhttp3.MultipartBody

class ListReviewFragment :
    BaseFragment<FragmentListReviewBinding>({ FragmentListReviewBinding.inflate(it) }) {
    var accessToken =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODE3OTA5LCJleHAiOjE3MjY0MDk5MDl9.D8cHYgTwnv-k3GdJpSexakAnn7rtZvML1cfkGm9qJoY"
    private val reviewList = ArrayList<ReviewModel>()
    private val reviewAdapter = ListReviewAdapter(reviewList)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stringList = listOf("CHARGING_SPEED")
        val imageList = listOf("string")

        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //평가하기 이동
        binding.tvGoElevation.setOnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .add(R.id)
        }

        //추천순으로 정렬
        binding.sortReviewLike.setOnClickListener {
            binding.sortReviewLikeIv.resources.getColor(R.color.gray_scale8, null)
            binding.sortReviewLatestIv.resources.getColor(R.color.gray_scale6, null)


            ReviewRepository.getReviewsStation(
                accessToken,
                1, 0, "SCORE", 10
            ) {response ->
                binding.tvReviewTotalCount.text = (response?.size ?: 0).toString()
                if (response != null) {
                    reviewList.addAll(response)
                }
                reviewAdapter.notifyDataSetChanged()
            }

        }


        //최신순으로 정렬
        binding.sortReviewLatest.setOnClickListener {
            binding.sortReviewLikeIv.resources.getColor(R.color.gray_scale6, null)
            binding.sortReviewLatestIv.resources.getColor(R.color.gray_scale8, null)

            ReviewRepository.getReviewsStation(
                accessToken,
                1, 0, "RECENT", 10
            ) {response ->
                binding.tvReviewTotalCount.text = (response?.size ?: 0).toString()
                reviewList.addAll(reviewList)
                reviewAdapter.notifyDataSetChanged()
            }

        }


        //이미지 업로드 테스트 보류
//        ReviewRepository.postImages(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//        )


//        ReviewRepository.postReview(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//            "콘텐트",
//            4.0,
//            stringList,
//            1,
//            imageList
//        )
//        ReviewRepository.getReviewInfo(
//                        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//6
//        ) {
//
//        }
//        ReviewRepository.recommendReview(
//                                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//6
//        )
//        ReviewRepository.deleteReview(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//4
//            )
//        ReviewRepository.editReview(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//6, "충전 속도가 빨라요 (수정)", 3.0, stringList, imageList
//            ){
//
//        }
//        ReviewRepository.getMyReviews(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//            ) {
//
//        }
//        ReviewRepository.getReviewKeywords(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//            ) {
//
//        }
    }
}