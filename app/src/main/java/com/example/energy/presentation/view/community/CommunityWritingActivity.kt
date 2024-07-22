package com.example.energy.presentation.view.community

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.community.WritingCommunityImage
import com.example.energy.databinding.ActivityCommunityWritingBinding
import com.example.energy.databinding.DialogSelectCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


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

        // 카테고리 선택 버튼 클릭 리스너 설정
        binding.communityWritingCategoryUnderBtn.setOnClickListener {
            showCategoryDialog()
        }

        // 이미지 업로드 사진 클릭 시
        binding.communityWritingImageSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) //갤러리 호출
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //멀티 선택 기능
            activityResult.launch(intent)
        }
    }
    private fun showCategoryDialog() { //카테고리 선택 화면
        val dialog = Dialog(this)
        val binding = DialogSelectCategoryBinding.inflate(layoutInflater)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 기존 다이어그램 배경 투명으로 적용(커스텀한 배경이 보이게 하기 위함)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setContentView(binding.root)
        dialog.setCancelable(true) // 바깥 영역 터치하면 닫힘

        // 다이얼로그 표시
        dialog.show()
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