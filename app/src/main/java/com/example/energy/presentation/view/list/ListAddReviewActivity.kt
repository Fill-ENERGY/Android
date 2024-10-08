package com.example.energy.presentation.view.list

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.energy.R

import com.example.energy.data.repository.review.ReviewRepository
import com.example.energy.databinding.ActivityListAddReviewBinding
import com.example.energy.databinding.DialogPostCommunityCancelBinding
import com.example.energy.databinding.DialogPostCommunitySuccessBinding
import com.example.energy.presentation.view.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class ListAddReviewActivity :
    BaseActivity<ActivityListAddReviewBinding>({ ActivityListAddReviewBinding.inflate(it) }) {
    private var score: Double = 0.0
    var keywordList = mutableSetOf<String>()
    var imageUris: List<Uri>? = null
    var imageStrings: List<String>? = null

    private val pickMultipleMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
//        if (result.resultCode == RESULT_OK) {
//            val imagePath = result.data!!.data
//
//
//            val file = File(imagePath?.let { absolutelyPath(it) })
//            val requestFile = file.asRequestBody(MediaType.parse("image/*"))
//            val body = MultipartBody.Part.createFormData("images", file.name, requestFile)
//
//        }
        lifecycleScope.launch {
            if(uris.isNotEmpty()) {
                var images : List<MultipartBody.Part> = ArrayList()
                val file : File? = uris?.let { getFileFromUri(it) }
                val requestFile = file?.asRequestBody("image/*".toMediaTypeOrNull())
                val data = file.let {
                    //이미지 업로드 테스트 보류
//        ReviewRepository.postImages(
//            accessToken, it
//        ) {
//
//        }

                }
                val body = requestFile?.let {
                    MultipartBody.Part.createFormData("images", file?.name,
                        it
                    )
                }

//            imageUris = uris
//            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            for (uri in uris) {
//                this.contentResolver.takePersistableUriPermission(uri, flag)
//            }
                Log.d("PhotoPicker테스트", "Number of items selected: ${uris.size}")
            } else {
                Log.d("PhotoPicker테스트", "No media selected")
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val stationId = intent.getIntExtra("stationId", -1)
        val stationName = intent.getStringExtra("stationName")
        Log.d("stationId", stationId.toString())

        //토큰 가져오기
        var sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = sharedPreferences?.getString("accessToken", "none")

        //충전소 이름 설정
        binding.tvStationName.text = stationName

        //스코어 조절
        val scoreImages = listOf(
            binding.ivScore,
            binding.ivScore2,
            binding.ivScore3,
            binding.ivScore4,
            binding.ivScore5
        )

        scoreImages.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                updateScore(index + 1)
                binding.tvScore.text = "(${index+1}/5)"
                Log.d("score텟트", score.toString())
            }
        }

        //키워드 설정
        //키워드 매핑
        val keywordViews = mapOf(
            "CHARGING_SPEED" to Pair(binding.cvKeywordChargingSpeed, binding.tvKeywordChargingSpeed),
            "ACCESSIBILITY" to Pair(binding.cvKeywordAccessibility, binding.tvKeywordAccessibility),
            "WAITING_AREA" to Pair(binding.cvKeywordWaitingArea, binding.tvKeywordWaitingArea),
            "CHARGER_MANAGEMENT" to Pair(binding.cvKeywordChargerManagement, binding.tvKeywordChargerManagement),
            "FACILITY_SUPPORT" to Pair(binding.cvKeywordFacilitySupport, binding.tvKeywordFacilitySupport),
            "REVISIT_INTENTION" to Pair(binding.cvKeywordRevisitIntention, binding.tvKeywordRevisitIntention)
        )

        // 각 CardView 클릭 리스너 설정
        for ((keyword, views) in keywordViews) {
            val (cardView, textView) = views
            cardView.setOnClickListener {
                handleKeywordClick(keyword, cardView, textView)
                Log.d("stringList텟트", keywordList.toList().toString())
            }
        }

        //키워드 초기화
        binding.cvNoKeyword.setOnClickListener {
            resetKeywords(keywordViews)
            Log.d("stringList텟트", keywordList.toList().toString())
        }

        //이미지 업로드
        binding.ivImageSelect.setOnClickListener {
//            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            intent.type = "image/*"
//            chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
//            chooserIntent.putExtra(Intent.EXTRA_TITLE,"사용할 앱을 선택해주세요.")
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            //imageStrings = convertUriListToStringList(imageUris!!)
            //Log.d("이미지 변환 테스트", imageStrings.toString())
        }

        val imageFiles = listOf(
            File("png"),
        )

//        // MultipartBody.Part 목록으로 변환합니다.
//        val imageParts = imageFiles.map { file ->
//            prepareFilePart("images", file) // "images"는 API에서의 파라미터 이름입니다.
//        }

        //뒤로 가기 버튼
        binding.ivBack.setOnClickListener {
            if(binding.etContent.text.isNullOrEmpty() &&
                imageFiles.isEmpty()){
                finish()
            } else{
                showCancelDialog()
            }
        }

        //내용 변화 listener
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        //평가 업로드 버튼
        binding.tvSubmit.setOnClickListener {
            if(binding.etContent.text.isNotEmpty()) {
                ReviewRepository.postReview(
                    accessToken!!,
                    binding.etContent.text.toString(),
                    score,
                    keywordList.toList(),
                    stationId,
                    null,
                )
                showSuccessDialog()
            } else {
                showToast("텍스트를 입력해주세요") //텍스트 필수
            }
        }

    }

    //
    private suspend fun getFileFromUri(uris: List<Uri>): File? = withContext(Dispatchers.IO) {
        try {
            for (uri in uris) {
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                inputStream?.let {
                    val file = createTempImageFile()
                    val outputStream = FileOutputStream(file)
                    copyInputStreamToFile(inputStream, outputStream)
                    return@withContext file
                }
            }
            return@withContext null
        } catch (e: IOException) {
            return@withContext null
        }
    }

    private fun createTempImageFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "temp_image",
            ".jpg",
            storageDir
        )
    }

    private fun copyInputStreamToFile(inputStream: InputStream, outputStream: FileOutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
    }

    //uri -> String으로 변환
    fun convertUriListToStringList(uriList: List<Uri>): List<String> {
        return uriList.map { it.toString() }
    }

    //키워드
    private fun handleKeywordClick(keyword: String, cardView: CardView, textView: TextView) {
        // 현재 색상 상태를 확인
        val isSelected = keywordList.contains(keyword)

        if (isSelected) {
            // 선택 해제 상태로 돌아가는 경우
            cardView.background.setTint(this.resources.getColor(R.color.gray_scale2))
            textView.setTextColor(Color.parseColor("#4A4945"))
            keywordList.remove(keyword)
        } else {
            // 선택 상태로 변경하는 경우
            cardView.background.setTint(this.resources.getColor(R.color.main_orange))
            textView.setTextColor(Color.parseColor("#FFFFFF"))
            keywordList.add(keyword)
        }
    }

    //키워드 초기화
    private fun resetKeywords(keywordViews: Map<String, Pair<CardView, TextView>>) {
        keywordList.clear()

        // 모든 CardView와 TextView의 색상을 디폴트로 변경
        for ((keyword, views) in keywordViews) {
            val (cardView, textView) = views
            cardView.background.setTint(this.resources.getColor(R.color.gray_scale2))
            textView.setTextColor(Color.parseColor("#4A4945"))
        }
    }

    //score 점수 업데이트
    private fun updateScore(selectedStar: Int) {
        val scoreImages = listOf(
            binding.ivScore,
            binding.ivScore2,
            binding.ivScore3,
            binding.ivScore4,
            binding.ivScore5
        )

        score = selectedStar.toDouble()
        for (i in scoreImages.indices) {
            val starImage = scoreImages[i]
            if (i < selectedStar) {
                starImage.setImageResource(R.drawable.iv_fill_star) // Filled star image
            } else {
                starImage.setImageResource(R.drawable.iv_unfill_star) // Empty star image
            }
        }
    }

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

    private fun absolutelyPath(path: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c= contentResolver?.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)
        return result!!
    }
    }