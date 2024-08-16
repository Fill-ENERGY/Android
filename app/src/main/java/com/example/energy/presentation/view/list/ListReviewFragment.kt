package com.example.energy.presentation.view.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.energy.data.repository.review.ReviewRepository
import com.example.energy.databinding.FragmentListReviewBinding
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.view.base.BaseFragment
import okhttp3.MultipartBody

class ListReviewFragment :
    BaseFragment<FragmentListReviewBinding>({ FragmentListReviewBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val stringList = listOf("CHARGING_SPEED")
        val imageList = listOf("string")

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
//        ReviewRepository.getReviewsStation(
//            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzcyNTkxLCJleHAiOjE3MjM3NzYxOTF9.5MvJ8wNXk44m6r4LIMGTlIUvWeUw6xlDuRhL_P73v9g",
//1, 0, "SCORE", 10
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