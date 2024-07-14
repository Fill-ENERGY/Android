package com.example.energy.presentation.view.community

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.databinding.ActivityCommunityWritingBinding


class CommunityWritingActivity : AppCompatActivity(), WritingCommunityImageRVAdapter.MyItemClickListener {
    private lateinit var binding: ActivityCommunityWritingBinding
    var list = ArrayList<WritingCommunityImage>() //선택한 이미지 데이터 리스트
    val adapter = WritingCommunityImageRVAdapter(list) //Recycler View Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recycler View & Adapter 연결
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.selectedImagesContainer.layoutManager = layoutManager
        binding.selectedImagesContainer.adapter = adapter

        // 어댑터에 클릭 리스너 설정
        adapter.setMyItemClickListener(this)

        // 이미지 업로드
        val selectImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    // 다중 이미지 선택
                    if (data?.clipData != null) {
                        val count = data.clipData?.itemCount ?: 0
                        for (i in 0 until count) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            addImageToList(imageUri)
                        }
                    }
                    // 단일 이미지 선택
                    else if (data?.data != null) {
                        val imageUri: Uri? = data.data
                        if(imageUri !=null) {
                            addImageToList(imageUri)
                        }
                    }
                }
                adapter.notifyDataSetChanged()

            }

        // 뒤로가기 버튼
        binding.communityWritingBackIcon.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // 이미지 업로드 버튼 클릭 시
        binding.communityWritingImageSelect.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "*/*"
            selectImagesActivityResult.launch(intent)
        }
    }

    private fun addImageToList(imageUri: Uri) { //데이터 리스트에 업로드하는 이미지 저장
        list.add(WritingCommunityImage(imageUri))
        adapter.notifyItemInserted(list.size - 1)
    }

    override fun onRemoveImage(position: Int) { //이미지 삭제
        adapter.removeImage(position)
    }

}