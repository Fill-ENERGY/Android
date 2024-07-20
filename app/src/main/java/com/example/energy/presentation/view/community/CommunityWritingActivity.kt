package com.example.energy.presentation.view.community

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.data.repository.community.WritingCommunityImage
import com.example.energy.databinding.ActivityCommunityWritingBinding


class CommunityWritingActivity : AppCompatActivity(), GalleryAdapter.MyItemClickListener {
    private lateinit var binding: ActivityCommunityWritingBinding
    var imageList = ArrayList<WritingCommunityImage>() //선택한 이미지 데이터 리스트
    val adapter = GalleryAdapter(imageList) //Recycler View Adapter

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

        // 뒤로가기 버튼
        binding.communityWritingBackIcon.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // 이미지 업로드 사진 클릭 시
        binding.communityWritingImageSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) //갤러리 호출
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //멀티 선택 기능
            activityResult.launch(intent)
        }
    }

    // 이미지 가져오기
    private var activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        //결과 코드 OK, 결과값 null 아니면 (이미지를 선택하면..)
        if (it.resultCode == RESULT_OK){
            //멀티 선택은 clip Data
            if (it.data!!.clipData != null) { //멀티 이미지
                val count = it.data!!.clipData!!.itemCount //선택한 이미지 갯수
                for(index in 0 until count){
                    val imageUri = it.data!!.clipData!!.getItemAt(index).uri //이미지 담기
                    addImageToList(imageUri) //이미지 추가
                }
            } else{ //싱글 이미지
                val imageUri = it.data!!.data
                addImageToList(imageUri!!)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun addImageToList(imageUri: Uri) { //데이터 리스트에 업로드하는 이미지 저장
        val isRepresentative = imageList.isEmpty() // 첫 번째 이미지인 경우 대표 이미지로 설정
        imageList.add(WritingCommunityImage(imageUri, isRepresentative))
        adapter.notifyItemInserted(imageList.size - 1)
    }

    override fun onRemoveImage(position: Int) { //이미지 삭제
        adapter.removeImage(position)
        // 대표 이미지가 삭제된 경우 새로운 대표 이미지 설정
        if (imageList.isNotEmpty() && !imageList.any { it.isRepresentative }) {
            imageList[0].isRepresentative = true
            adapter.notifyItemChanged(0)
        }
    }

}