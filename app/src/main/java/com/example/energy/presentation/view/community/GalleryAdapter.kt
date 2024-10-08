package com.example.energy.presentation.view.community

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.data.repository.community.ImagesModel
import com.example.energy.databinding.ItemWritingCommunityImageBinding


class GalleryAdapter (private val imageUrl: MutableMap<String, Boolean>): RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private val imageUris: List<String> get() = imageUrl.keys.toList()

    interface MyItemClickListener{
        fun onRemoveImage(position: Int)
    }

    // Listener 객체를 Adapter에서 사용할 수 있도록 따로 저장할 변수 선언
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GalleryAdapter.ViewHolder {
        // itemview 객체 생성
        val binding: ItemWritingCommunityImageBinding = ItemWritingCommunityImageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryAdapter.ViewHolder, position: Int) {
        val currentImage = imageUris[position]

        Glide.with(holder.itemView.context)
            .load(imageUris[position]) //이미지 위치
            .into(holder.galleryView) //보여줄 위치

        // 대표 이미지 표시
        holder.binding.representativeLabel.visibility = if (imageUrl[currentImage] == true) View.VISIBLE else View.GONE
        //(currentImage.isRepresentative)

        // X 아이콘 클릭 시 해당데이터 삭제
        holder.binding.writingCommunityImageCancel.setOnClickListener {
            mItemClickListener.onRemoveImage(position)
        }
    }

    override fun getItemCount(): Int = imageUrl.size

    @SuppressLint("NotifyDataSetChanged")
    fun removeImage(position: Int) {
        if (position in imageUris.indices) {
            val keyToRemove = imageUris[position]
            imageUrl.remove(keyToRemove)

            if (imageUrl.isNotEmpty()) {
                // 새로운 대표 이미지 설정
                val newRepresentative = imageUrl.keys.first()
                imageUrl[newRepresentative] = true
            }

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    inner class ViewHolder(val binding: ItemWritingCommunityImageBinding): RecyclerView.ViewHolder(binding.root) {
        val galleryView : ImageView = binding.itemWritingCommunityImage
//        fun bind(image: WritingCommunityImage) {
//            binding.itemWritingCommunityImage.setImageURI(image.imageUrl)
//        }
    }

}