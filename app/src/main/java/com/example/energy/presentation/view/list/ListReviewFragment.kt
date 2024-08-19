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
import com.example.energy.data.repository.map.search.SearchData
import com.example.energy.data.repository.review.ReviewModel
import com.example.energy.data.repository.review.ReviewRepository
import com.example.energy.databinding.FragmentListReviewBinding
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.view.base.BaseFragment
import com.example.energy.presentation.view.map.MapFragment
import com.example.energy.presentation.view.map.SearchAdapter
import com.example.energy.presentation.view.map.SearchResultFragment
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ListReviewFragment :
    BaseFragment<FragmentListReviewBinding>({ FragmentListReviewBinding.inflate(it) }) {
    var accessToken =
        "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODg3ODYzLCJleHAiOjE3MjY0Nzk4NjN9.qGR9PibGimGon0_82i_Z73nxXJzK1BDoPLWRLjC0QI4"
    private val reviewList = ArrayList<ReviewModel>()
    private val reviewAdapter = ListReviewAdapter(reviewList)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stringList = listOf("CHARGING_SPEED")

        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //추천순으로 정렬
        binding.sortReviewLike.setOnClickListener {
            sortReview("SCORE")
        }

        //최신순으로 정렬
        binding.sortReviewLatest.setOnClickListener {
            sortReview("RECENT")
        }

        //리사이클러뷰 클릭 이벤트
        reviewAdapter.setItemClickListener(object : ListReviewAdapter.OnItemClickListener {
            //리뷰 추천
            override fun onRecommendReview(reviewId: Int) {
                ReviewRepository.recommendReview(
                    accessToken, reviewId
                )
            }
        })

        val imageFiles = listOf(
            File("png"),
        )

        // MultipartBody.Part 목록으로 변환합니다.
        val imageParts = imageFiles.map { file ->
            prepareFilePart("images", file) // "images"는 API에서의 파라미터 이름입니다.
        }

//        //이미지 업로드 테스트 보류
//        ReviewRepository.postImages(
//            accessToken, imageParts
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
    fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun sortReview(sort: String) {
        ReviewRepository.getReviewsStation(
            accessToken,
            1, 0, sort, 10
        ) { response ->
            binding.tvReviewTotalCount.text = (response?.size ?: 0).toString()
            if (response != null) {
                reviewList.clear()
                reviewList.addAll(response)
                reviewAdapter.notifyDataSetChanged()
            }
        }
    }
}