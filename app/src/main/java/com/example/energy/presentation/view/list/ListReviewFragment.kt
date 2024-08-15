package com.example.energy.presentation.view.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.energy.data.repository.review.ReviewRepository
import com.example.energy.databinding.FragmentListReviewBinding
import com.example.energy.databinding.FragmentMapBinding
import com.example.energy.presentation.view.base.BaseFragment

class ListReviewFragment : BaseFragment<FragmentListReviewBinding>({ FragmentListReviewBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        ReviewRepository.postReview()
//        ReviewRepository.getReviewInfo()
//        ReviewRepository.recommendReview()
//        ReviewRepository.deleteReview()
//        ReviewRepository.editReview()
//        ReviewRepository.postImages()
//        ReviewRepository.getMyReviews()
//        ReviewRepository.getReviewsStation()
//        ReviewRepository.getReviewKeywords()
    }
}