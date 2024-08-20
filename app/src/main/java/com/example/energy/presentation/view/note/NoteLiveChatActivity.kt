package com.example.energy.presentation.view.note

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.energy.R
import com.example.energy.data.repository.note.MessageRequest
import com.example.energy.data.repository.note.NoteRepository
import com.example.energy.databinding.ActivityNoteLiveChatBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

class NoteLiveChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteLiveChatBinding

    private var lastDisplayedDate: String? = null

    private var lastTimeTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //토큰 가져오기
        //var sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InduZGtkdXMxMDJAbmF2ZXIuY29tIiwiaWF0IjoxNzI0MTMxNjc3LCJleHAiOjE3MjY3MjM2Nzd9.NT0iEfaOANA8m1Y5E8p0-4ZwuUYBZdMQkHhYVj5X7jA"

        binding = ActivityNoteLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 threadId, receiverId, username 및 userId 가져오기
        val threadId = intent.getIntExtra("threadId", -1)
        val receiverId = intent.getIntExtra("receiverId", -1)
        val username = intent.getStringExtra("Username") ?: "Unknown User"
        val userId = intent.getStringExtra("Id") ?: "Unknown ID"



        /*


        // username, Id 가져오기

        val username = intent.getStringExtra("Username") ?: "김규리"
        val userId = intent.getStringExtra("Id") ?: "rlarbfl"


         */


        binding.usernameTextView.text = username
        binding.userIdTextView.text = userId


        //뒤로 가기 버튼 클릭 시
        binding.backbutton.setOnClickListener {
            finish()
        }


        //전송 버튼 클릭 시

        binding.sendButton.setOnClickListener {
            sendMessage(accessToken, threadId, receiverId)
        }


        // 프로필로 전환하는 클릭 리스너
        val clickListener = View.OnClickListener {
            val intent = Intent(this, NoteUserProfileActivity::class.java).apply {
                putExtra("Username", username)
                putExtra("Id", userId)
            }
            startActivity(intent)
        }


        //프로필 전환 클릭 리스너 적용
        binding.usernameTextView.setOnClickListener(clickListener)
        binding.userIdTextView.setOnClickListener(clickListener)
        binding.UserProfile.setOnClickListener(clickListener)
        binding.showMoreButton.setOnClickListener(clickListener)




    }





    private fun sendMessage(accessToken: String?, threadId: Int, receiverId: Int) {

        //val accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzk0MjY1LCJleHAiOjE3MjYzODYyNjV9.I1m8HjK_zT67iTM1rc9RvH57aoCkGjw6pSkaXACZzXA"
        val message = binding.messageInput.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        val receiverId = intent.getIntExtra("receiverId", 2)

        if (receiverId == -1) {
            Toast.makeText(this, "수신자 Id가 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }


        val threadId = intent.getIntExtra("threadId", 5)
        val images = emptyList<String>()

        val messageRequest = if (threadId > 0 ) {
            MessageRequest(threadId, message, images, receiverId)
        } else {
            MessageRequest(null, message, images, receiverId)
        }


        /*



        val messageRequest = MessageRequest(
            5,
            message,
            emptyList(),
            2
        )


         */




        NoteRepository.sendMessage(accessToken!!, messageRequest) { response ->
            if (response != null) {
                // 서버 응답 전체를 로그로 출력하여 문제를 확인
                Log.d("NoteLiveChatActivity", "서버 응답: ${response.result}")


                // 채팅 시간

                val createdAt = response.result.createdAt
                val dateFormat = formatDate(createdAt)
                val timeFormat = formatTime(createdAt)

                addChatBubble(message, dateFormat, timeFormat, true)
                binding.messageInput.text.clear()
                binding.chatScrollView.post {
                    binding.chatScrollView.fullScroll(View.FOCUS_DOWN)
                }
            } else {
                Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }






    /*
    private fun formatDate(createdAt: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = formatter.parse(createdAt) ?: Date()
        val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
        return outputFormat.format(date)
    }



     */


    // 시간 형식 변환 함수
    private fun formatDate(createdAt: String): String {

        return try {

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

            // 나노초를 제거한 후 파싱
            val date = simpleDateFormat.parse(createdAt.substring(0, 19)) ?: Date()

            // "년 월 일 " 형식으로 변환
            val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
            outputFormat.format(date)

        } catch (e: Exception) {
            e.printStackTrace()
            createdAt
        }
    }


    // 시간 형식 변환 함수
    private fun formatTime(createdAt: String): String {

        return try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = simpleDateFormat.parse(createdAt.substring(0, 19)) ?: Date()

            val timeFormat = SimpleDateFormat("a h:mm", Locale.KOREAN)
            timeFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""

        }
    }







    private fun addChatBubble(message: String, date: String, time: String, isUserMessage: Boolean) {

        // 처음 보낼 때만 시간 출력
        if (lastDisplayedDate != date) {

            lastDisplayedDate = date


            // 날짜를 표시하는 TextView 생성
            val dateTextView = TextView(this).apply {
                text = date
                gravity = Gravity.CENTER
                setPadding(0, 20, 0, 10)
                setTextColor(resources.getColor(android.R.color.darker_gray, null))
            }


            binding.chatContainer.addView(dateTextView)
        }


        // 연속으로 채팅 전송할 경우 -> 마지막 시간 표시를 제거
        lastTimeTextView?.let { binding.chatContainer.removeView(it) }







        val chatBubble = TextView(this).apply {
            text = message
            setBackgroundResource(if (isUserMessage) R.drawable.chat_bubble_background else R.drawable.chat_bubble_background) // 사용자 메시지와 상대방 메시지의 배경 구분
            setPadding(34, 22, 34, 22)
            setTextColor(resources.getColor(android.R.color.black, null))

            // 최대 너비 설정 (약 50자 정도의 너비)
            maxWidth = resources.displayMetrics.densityDpi * 250 / 160


            // 레이아웃 파라미터 설정
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // 메시지 방향에 따라 정렬 및 여백 조정
            if (isUserMessage) {
                layoutParams.gravity = Gravity.END // 오른쪽 정렬
                layoutParams.setMargins(50, 30, 8, 8) // 오른쪽에 여백을 둠
            } else {
                layoutParams.gravity = Gravity.START // 왼쪽 정렬
                layoutParams.setMargins(8, 8, 50, 8) // 왼쪽에 여백을 둠
            }

            this.layoutParams = layoutParams
        }

        // 시각 출력
        val timeTextView = TextView(this).apply {
            text = time
            gravity = Gravity.END
            setPadding(0, 4, 8, 8)
            setTextColor(resources.getColor(android.R.color.darker_gray, null))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.END // 시각을 오른쪽 끝에 정렬
            this.layoutParams = layoutParams
        }

        // 채팅 컨테이너에 버블과 시각 추가
        binding.chatContainer.addView(chatBubble)
        binding.chatContainer.addView(timeTextView)

        // 마지막으로 추가된 시간 표시를 추적
        lastTimeTextView = timeTextView
    }

}