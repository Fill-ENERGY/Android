package com.example.energy.presentation.view.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.databinding.ActivityCommunityDetailBinding

class CommunityDetailActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCommunityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 전달받은 데이터 수신
        val postInfo = intent.getParcelableExtra<CommunityPost>("postInfo")
        postInfo?.let {
            binding.communityDetailUserProfile.setImageResource(it.userProfile)
            binding.communityDetailUserName.text = it.userName
            binding.communityDetailTitle.text = it.title
            binding.communityDetailContent.text = it.content
            binding.communityDetailLikeNum.text = it.likes
            binding.communityDetailCommentNum.text = it.comments

            // 사진 리스트 처리 (예: Glide를 사용하여 이미지 로딩)
            // 이 부분은 필요에 따라 추가적으로 구현해야 합니다.
            it.imageUrl.forEachIndexed { index, uri ->
                // 이미지뷰에 Glide를 사용하여 Uri를 로드하는 예제
                // binding.detailImageViews[index].let { imageView -> Glide.with(this).load(uri).into(imageView) }
            }
        }

        binding.communityDetailBackIcon.setOnClickListener {
            finish()
        }
    }
}