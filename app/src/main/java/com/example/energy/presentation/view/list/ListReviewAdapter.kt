package com.example.energy.presentation.view.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.review.ReviewModel
import com.example.energy.databinding.ItemReviewBinding

class ListReviewAdapter(private val itemList: ArrayList<ReviewModel>): RecyclerView.Adapter<ListReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListReviewAdapter.ViewHolder {
        val binding: ItemReviewBinding = ItemReviewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListReviewAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position]) //받아온 데이터를 객체에 넣어주는 작업

        // 아이템 클릭 이벤트
//        holder.itemView.setOnClickListener {
//            itemClickListener.onItemClick(itemList[position])
//        }

        holder.binding.tvRecommendReview.setOnClickListener {
            itemClickListener.onRecommendReview(itemList[position].id)
        }
    }

    inner class ViewHolder(val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(reviewModel: ReviewModel) {
            binding.tvNickname.text = reviewModel.authorName
            binding.tvStarTotal.text = reviewModel.score.toString()
        }
    }

    interface OnItemClickListener {
        //리뷰 추천
        fun onRecommendReview(reviewId: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

}