package com.example.energy.presentation.view.community

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.databinding.ActivityCommunityWritingBinding
import com.example.energy.data.repository.community.CommunityRepository
import com.example.energy.data.repository.community.ImagesModel
import com.example.energy.data.repository.community.PostBoardRequest
import com.example.energy.data.repository.community.UploadImagesRequest
import com.example.energy.databinding.DialogPostCommunityCancelBinding
import com.example.energy.databinding.DialogPostCommunitySuccessBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.Multipart
import java.io.File

class CommunityWritingActivity : AppCompatActivity(), GalleryAdapter.MyItemClickListener {
    private lateinit var binding: ActivityCommunityWritingBinding
    var imageModel = mutableListOf<Multipart>()
    var imageList = mutableMapOf<String, Boolean>() // 선택한 이미지 데이터 리스트
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
    private var accessToken: String? = null
    private var postId: Int? = null // 수정할 게시글 ID (null이면 새 게시글)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWritingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //토큰 가져오기
//        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
//        accessToken = sharedPreferences?.getString("accessToken", "none")
        accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzOTg1OTUxLCJleHAiOjE3MjY1Nzc5NTF9.jEn8OyBau-JQ576OLgESOD0dGcGH614WfsQUGGbtq_M"


        // Intent로 전달된 postId 확인
        postId = intent.getIntExtra("postId", -1).takeIf { it != -1 }

        spinner = binding.communitySelectCategory
        titleEditText = binding.communityWritingTitleTv
        contentEditText = binding.communityWritingContent
        finishButton = binding.communityWritingFinishTv


        // 수정 모드일 경우, 기존 데이터 로드
        postId?.let { loadPostData(it) }


        // 제목 EditText 변화 listener
        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isTitleFilled = !s.isNullOrEmpty()
                updateFinishButtonState()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    // Hint 상태일 때의 스타일 지정
                    titleEditText.setTextAppearance(R.style.Title3)
                } else {
                    // 텍스트가 입력되었을 때의 스타일 지정
                    titleEditText.setTextAppearance(R.style.Title2)
                }
            }
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
            if(titleEditText.text.isNullOrEmpty() &&
                contentEditText.text.isNullOrEmpty() &&
                !isCategorySelected &&
                imageList.isEmpty()){
                finish()
            } else{
                showCancelDialog()
            }
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
                if (postId != null) {
                    updatePostWithImages(postId!!) // 게시글 수정
                } else {
                    savePostWithImages() // 새 게시글 작성
                }
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
        binding.activityCommunityWriting.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }


    // 게시글 수정 시 기존 데이터 로드 함수
    private fun loadPostData(postId: Int) {
        CommunityRepository.getDetailCommunity(accessToken ?: "none", postId) { response ->
            if (response != null) {
                // 기존 데이터 UI에 반영
                titleEditText.setText(response.title)
                contentEditText.setText(response.content)
                spinner.setSelection(menuList.indexOf(response.category))
                // 이미지 데이터 처리
                response.images.forEach { imageUri ->
                    addImageToList(imageUri)
                }
                adapter.notifyDataSetChanged()
            } else {
                Log.e("상세커뮤니티조회", "상세 게시글 데이터가 없습니다.")
            }
        }
    }


    // 게시글 수정 함수
    private fun updatePostWithImages(postId: Int) {
        val category: String = toEnglish(spinner.selectedItem.toString())
        val imageUriList: List<String> = imageList.keys.toList()

        val postBoardRequest = PostBoardRequest(
            title = titleEditText.text.toString(),
            content = contentEditText.text.toString(),
            category = category,
            images = imageUriList
        )

        CommunityRepository.updateBoard(accessToken ?: "none", postId, postBoardRequest) { response ->
            if (response != null) {
                Log.d("커뮤니티수정", "게시글 수정 성공: ${response.title}")
                showSuccessDialog()
            } else {
                Log.e("커뮤니티수정", "게시글 수정 실패: ${response}")
            }
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
            val clipData = it.data?.clipData //멀티 이미지
            val data = it.data?.data //단일 이미지

            if (clipData != null) { // 멀티 이미지 선택 시
                val count = clipData.itemCount // 선택한 이미지 갯수
                if (count > 10) {
                    Toast.makeText(this, "10장까지 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    for (index in 0 until count) {
                        val imageUri = clipData.getItemAt(index).uri // 이미지 담기
                        addImageToList(imageUri.toString())
                    }
                }
            } else if (data != null) { // 단일 이미지 선택 시
                addImageToList(data.toString())
            }

            adapter.notifyDataSetChanged()
        }
    }

    // 데이터 리스트에 업로드하는 이미지 저장
    private fun addImageToList(imageUri: String) {
        if (imageList.isEmpty()) {
            imageList[imageUri] = true // 첫 번째 이미지를 대표 이미지로 설정
        } else {
            imageList[imageUri] = false
        }
        adapter.notifyItemInserted(imageList.size - 1)
    }

    // 이미지 삭제
    override fun onRemoveImage(position: Int) {
        adapter.removeImage(position)

        // 대표 이미지가 삭제된 경우 새로운 대표 이미지 설정
        if (imageList.isNotEmpty() && !imageList.values.any { it }) {
            val firstKey = imageList.keys.first()
            imageList[firstKey] = true
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

    // 취소 확인 Dialog 띄우는 함수
    private fun showCancelDialog() {
        val dialog = Dialog(this, R.style.CustomDialog)
        val binding = DialogPostCommunityCancelBinding.inflate(layoutInflater)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 기존 다이어그램 배경 투명으로 적용(커스텀한 배경이 보이게 하기 위함)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(false) // 바깥 영역 터치해도 닫힘 X

        binding.exitButton.setOnClickListener {
            // 나가기 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
            finish() //액티비티 종료
        }

        binding.cancelButton.setOnClickListener {
            // 아니오(취소) 버튼 클릭 시 수행할 작업
            dialog.dismiss() // 다이얼로그 닫기
        }

        // 다이얼로그 표시
        dialog.show()
    }

    fun toEnglish(category: String): String{
        return when(category){
            "일상" -> "DAILY"
            "궁금해요" -> "INQUIRY"
            "도와줘요" -> "HELP"
            "휠체어" -> "WHEELCHAIR"
            else -> "SCOOTER"
        }
    }

    // 게시글 저장 함수
    private fun savePostWithImages() {
        //카테고리 -> 영어로 변환
        val category: String = toEnglish(spinner.selectedItem.toString())

        // 이미지 리스트 추출
        val imageUriList: List<String> = imageList.keys.toList() //이미지 String 리스트
        Log.d("이미지정보", "${imageUriList}")

        // 이미지 URI 리스트를 MultipartFile로 변환
        val imageParts = imageList.keys.map { imageUri ->
            val file = File(imageUri)  // 이미지 파일을 가져옴. (여기서는 URI를 파일 경로로 가정)
            val requestFile = file.asRequestBody("images".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, requestFile)
        }
        Log.d("이미지MultipartFile", "${imageParts}")


        // 게시글 작성 요청 데이터
        val postBoardRequest = PostBoardRequest(
            title = titleEditText.text.toString(),
            content = contentEditText.text.toString(),
            category = category,
            images = imageUriList // 이미지 리스트
        )

        // 이미지 업로드 요청 데이터
        val uploadImagesRequest = UploadImagesRequest(
            images = imageParts
        )

//        // 이미지 업로드 API 호출
//        CommunityRepository.uploadImages(accessToken?: "none", uploadImagesRequest) { uploadResponse  ->
//            if (uploadResponse != null) {
//                // 성공적으로 게시글이 작성됨
//                Log.d("이미지업로드", "이미지 업로드 성공: ${uploadResponse.images}")
//            } else {
//                // 게시글 작성 실패
//                Log.e("이미지업로드", "이미지 업로드 실패: ${uploadResponse}")
//            }
//        }
//
//        // 게시글 작성 API 호출
//        CommunityRepository.postBoard(accessToken?: "none", postBoardRequest) { uploadResponse  ->
//            if (uploadResponse != null) {
//                // 성공적으로 게시글이 작성됨
//                Log.d("커뮤니티업로드", "게시글 작성 성공: ${uploadResponse.title}")
//            } else {
//                // 게시글 작성 실패
//                Log.e("커뮤니티업로드", "게시글 작성 실패: ${uploadResponse}")
//            }
//        }


        if (imageUriList.isNotEmpty()) {
            // 이미지가 선택된 경우
            CommunityRepository.uploadImages(accessToken ?: "none", uploadImagesRequest) { uploadResponse ->
                if (uploadResponse != null) {
                    // 이미지 업로드 성공
                    Log.d("이미지업로드", "이미지 업로드 성공: ${uploadResponse.images}")

                    // 게시글 작성 API 호출
                    CommunityRepository.postBoard(accessToken ?: "none", postBoardRequest) { uploadResponse ->
                        if (uploadResponse != null) {
                            // 게시글 작성 성공
                            Log.d("커뮤니티업로드", "게시글 작성 성공: ${uploadResponse.title}")
                            showSuccessDialog()
                        } else {
                            // 게시글 작성 실패
                            Log.e("커뮤니티업로드", "게시글 작성 실패: ${uploadResponse}")
                        }
                    }
                } else {
                    // 이미지 업로드 실패
                    Log.e("이미지업로드", "이미지 업로드 실패: ${uploadResponse}")
                }
            }
        } else {
            // 이미지가 선택되지 않은 경우, 바로 게시글 작성 API 호출
            CommunityRepository.postBoard(accessToken ?: "none", postBoardRequest) { uploadResponse ->
                if (uploadResponse != null) {
                    // 게시글 작성 성공
                    Log.d("커뮤니티업로드", "게시글 작성 성공: ${uploadResponse.title}")
                    showSuccessDialog()
                } else {
                    // 게시글 작성 실패
                    Log.e("커뮤니티업로드", "게시글 작성 실패: ${uploadResponse}")
                }
            }
        }

        //showSuccessDialog()



//        val imageUriList: List<Uri> = imageList.map { it.imageUrl }

//        val category: Int = fromString(spinner.selectedItem.toString())
//
//        // CommunityPost 객체 생성
//        val newPost = CommunityPost(
//            userProfile = R.drawable.userimage, // 예시로 설정된 값
//            userName = "사용자 이름", // 예시로 설정된 값
//            title = titleEditText.text.toString(),
//            content = contentEditText.text.toString(),
//            categoryString = spinner.selectedItem.toString(),
//            category = category,
//            imageUrl = imageUriList, //imageUriList, // 이미지 Uri 리스트 저장
//            likes = "0",
//            comments = "0"
//        )
    }
}
