package com.example.energy.presentation.view.community

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.energy.R
import com.example.energy.data.repository.community.WritingCommunityImage
import com.example.energy.databinding.ActivityCommunityWritingBinding
import com.example.energy.databinding.DialogPostCommunitySuccessBinding
import com.example.energy.data.CommunityPostDatabase
import com.example.energy.data.repository.community.CommunityPost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommunityWritingActivity : AppCompatActivity(), GalleryAdapter.MyItemClickListener {
    private lateinit var binding: ActivityCommunityWritingBinding
    private lateinit var communityDB: CommunityPostDatabase
    var imageList = ArrayList<WritingCommunityImage>() // 선택한 이미지 데이터 리스트
    var postInfo = ArrayList<CommunityPost>()
    val adapter = GalleryAdapter(imageList) // Recycler View Adapter
    val initText = "카테고리를 선택해 주세요"
    private val menuList = listOf("카테고리를 선택해 주세요", "일상","궁금해요","도와줘요","휠체어", "스쿠터")

    private lateinit var spinner: Spinner
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var finishButton: TextView
    private var isCategorySelected = false
    private var isTitleFilled = false
    private var isContentFilled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Room 데이터베이스 인스턴스 생성
        communityDB = CommunityPostDatabase.getInstance(this)!!

        spinner = binding.communitySelectCategory
        titleEditText = binding.communityWritingTitleTv
        contentEditText = binding.communityWritingContent
        finishButton = binding.communityWritingFinishTv

        // 제목 EditText 변화 listener
        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isTitleFilled = !s.isNullOrEmpty()
                updateFinishButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 내용 EditText 변화 listener
        contentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isContentFilled = !s.isNullOrEmpty()
                updateFinishButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 사진 Recycler View & Adapter 연결
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.selectedImagesContainer.layoutManager = layoutManager
        binding.selectedImagesContainer.adapter = adapter

        // 사진 어댑터에 클릭 리스너 설정
        adapter.setMyItemClickListener(this)

        // 뒤로가기 버튼
        binding.communityWritingBackIcon.setOnClickListener {
            finish() // 현재 액티비티 종료
        }

        // Spinner Adapter 연결
        val spinnerAdapter = OptionSpinnerAdapter(this, R.layout.item_select_category, menuList, initText)
        binding.communitySelectCategory.setAdapter(spinnerAdapter)
        binding.communitySelectCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                spinnerAdapter.setSelectedPosition(position) // 선택한 항목 설정
                isCategorySelected = true
                updateFinishButtonState()

                val value = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@CommunityWritingActivity, value, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택되지 않은 경우 처리
                isCategorySelected = false
                updateFinishButtonState()
            }
        }

        // 등록 버튼 click listener
        finishButton.setOnClickListener {
            if (finishButton.isEnabled) {
                savePostWithImages() // 게시글 저장
            }
        }

        // 이미지 업로드 사진 클릭 시
        binding.communityWritingImageSelect.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // 갤러리 호출
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 멀티 선택 기능
            activityResult.launch(intent)
        }

        // 키보드 외부 화면 클릭 시 키보드 숨기기
        binding.activityCommunityWriting.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

    // 등록 버튼 상태 업데이트하는 함수
    private fun updateFinishButtonState() {
        finishButton.isEnabled = isTitleFilled && isContentFilled && isCategorySelected
        if (finishButton.isEnabled) {
            finishButton.setTextColor(ContextCompat.getColor(applicationContext!!, R.color.gray_scale8))
        } else {
            finishButton.setTextColor(ContextCompat.getColor(applicationContext!!, R.color.gray_scale5))
        }

    }

    // 이미지 가져오는 함수
    private var activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // 결과 코드 OK, 결과값 null 아니면 (이미지를 선택하면..)
        if (it.resultCode == RESULT_OK) {
            // 멀티 선택은 clip Data
            if (it.data!!.clipData != null) { // 멀티 이미지
                val count = it.data!!.clipData!!.itemCount // 선택한 이미지 갯수
                for (index in 0 until count) {
                    val imageUri = it.data!!.clipData!!.getItemAt(index).uri // 이미지 담기
                    addImageToList(imageUri) // 이미지 추가
                }
            } else { // 싱글 이미지
                val imageUri = it.data!!.data
                addImageToList(imageUri!!)
            }
            adapter.notifyDataSetChanged()
        }
    }

    // 데이터 리스트에 업로드하는 이미지 저장
    private fun addImageToList(imageUri: Uri) {
        val isRepresentative = imageList.isEmpty() // 첫 번째 이미지인 경우 대표 이미지로 설정
        imageList.add(WritingCommunityImage(imageUri, isRepresentative))
        adapter.notifyItemInserted(imageList.size - 1)
    }

    // 이미지 삭제
    override fun onRemoveImage(position: Int) {
        adapter.removeImage(position)
        // 대표 이미지가 삭제된 경우 새로운 대표 이미지 설정
        if (imageList.isNotEmpty() && !imageList.any { it.isRepresentative }) {
            imageList[0].isRepresentative = true
            adapter.notifyItemChanged(0)
        }
    }

    // 키보드 내리는 함수
    private fun hideKeyboard() {
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocusView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    // 정상 등록 Dialog 띄우는 함수
    private fun showSuccessDialog() {
        val dialog = Dialog(this, R.style.CustomDialog)
        val binding = DialogPostCommunitySuccessBinding.inflate(layoutInflater)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 기존 다이어그램 배경 투명으로 적용(커스텀한 배경이 보이게 하기 위함)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false) // 바깥 영역 터치해도 닫힘 X

        binding.dialogPostSuccessBtn.setOnClickListener {
            // 확인 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
            finish()
        }

        // 다이얼로그 표시
        dialog.show()
    }

    // 게시글 저장 함수
    private fun savePostWithImages() {

        // 이미지 Uri 리스트 추출
        val imageUriList: List<Uri> = imageList.map { it.imageUrl }

        // CommunityPost 객체 생성
        val newPost = CommunityPost(
            userProfile = R.drawable.user_profile, // 예시로 설정된 값
            userName = "사용자 이름", // 예시로 설정된 값
            title = titleEditText.text.toString(),
            content = contentEditText.text.toString(),
            category = spinner.selectedItem.toString(),
            imageUrl = imageUriList, // 이미지 Uri 리스트 저장
            likes = "0",
            comments = "0"
        )

        // 데이터베이스에 저장 (비동기 작업)
        communityDB.communityPostDao().insertPost(newPost)
        postInfo.addAll(communityDB.communityPostDao().getAllPosts())
        Log.d("community", communityDB.communityPostDao().getAllPosts().toString())
        showSuccessDialog()
    }
}
