package com.example.energy.presentation.view.note

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.energy.R
import com.example.energy.data.repository.note.GetDetailMessage
import com.example.energy.data.repository.note.MessageRequest
import com.example.energy.data.repository.note.NoteRepository
import com.example.energy.data.repository.note.NoteRepository.Companion.sendMessage
import com.example.energy.data.repository.note.NoteRepository.Companion.updateReadMessage
import com.example.energy.databinding.ActivityNoteLiveChatBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale

class NoteLiveChatActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNoteLiveChatBinding

    private var cursor: Int = 0

    private var getunreadmessage: Int = 0

    private var lastDisplayedDate: String? = null

    private var lastTimeTextView: TextView? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 토큰 가져오기
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")

        binding = ActivityNoteLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 threadId, receiverId, username 및 userId 가져오기
        val threadId = intent.getIntExtra("threadId", -1)
        val receiverId = intent.getIntExtra("receiverId", -1)
        val username = intent.getStringExtra("Username") ?: "Unknown User"
        val userId = intent.getStringExtra("Id") ?: "Unknown ID"
        getunreadmessage = intent.getIntExtra("unreadMessageCount", -1)
        cursor = intent.getIntExtra("cursor", 0)


        if (getunreadmessage == 0) {
            GetMessages(threadId, cursor+1, receiverId)
        } else {
            UpdateReadMessages(threadId, cursor+1, receiverId)
        }





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


    // 쪽지 목록 조회

    private fun GetMessages(threadId: Int, cursor: Int, receiverId: Int)
    {

        // 토큰 가져오기
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")

        //threadId = threadId +1
        NoteRepository.getMessages(accessToken!!, threadId, cursor , 100) { response ->
            if (response.result?.messages != null) {



                // 메시지 리스트를 역순으로  displayChatMessages 함수로 전달하여 UI에 표시
                displayChatMessages(response.result.messages.reversed(), receiverId)





            } else {
                Toast.makeText(this, "쪽지 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }


        }

    }

    // 안 읽은 메시지가 있을 때
    private fun UpdateReadMessages(threadId: Int, cursor: Int, receiverId: Int)
    {

        // 토큰 가져오기
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")

        //threadId = threadId +1
        NoteRepository.updateReadMessage(accessToken!!, threadId, cursor , 100) { response ->
            if (response.result?.messages != null) {



                // 메시지 리스트를 역순으로  displayChatMessages 함수로 전달하여 UI에 표시
                displayChatMessages(response.result.messages.reversed(), receiverId)





            } else {
                Toast.makeText(this, "쪽지 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }


        }

    }

    // 메시지를 UI에 표시하는 함수
    private fun displayChatMessages(messages: List<GetDetailMessage>, receiverId: Int) {
        messages.forEach { message ->
            val dateFormat = formatDate(message.createdAt ?: "")
            val timeFormat = formatTime(message.createdAt ?: "")

            // receiverId 와 sender가 같을 경우 상대방이 보낸 쪽지
            var isUserMessage = true
            if (receiverId == message.sender ) {
                isUserMessage = false
            }


            message.content?.let { addChatBubble(it, dateFormat, timeFormat, isUserMessage) }



        }



        // 모든 메시지를 추가한 후 스크롤을 맨 아래로 이동
        binding.chatScrollView.post {
            binding.chatScrollView.fullScroll(View.FOCUS_DOWN)
        }
    }





    // 메시지 전송 api

    private fun sendMessage(accessToken: String?, threadId: Int, receiverId: Int) {

          val message = binding.messageInput.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }



        if (receiverId == -1) {
            Toast.makeText(this, "수신자 Id가 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }



        val images = emptyList<String>()

        val messageRequest = if (threadId > 0 ) {
            MessageRequest(threadId, message, images, receiverId)
        } else {
            MessageRequest(null, message, images, receiverId)
        }




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

                //cursor 값 최신 값으로
                cursor = response.result.messageId!!




            } else {
                Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }






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
            createdAt

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
                setTextAppearance(R.style.Body4)
                setPadding(0, 20, 0, 10)
                setTextColor(resources.getColor(R.color.gray_scale7, null))
            }


            binding.chatContainer.addView(dateTextView)
        }


        // 연속으로 채팅 전송할 경우 -> 마지막 시간 표시를 제거
        lastTimeTextView?.let { binding.chatContainer.removeView(it) }





        // 채팅 내용 출력

        val chatBubble = TextView(this).apply {
            text = message
            setBackgroundResource(if (isUserMessage) R.drawable.chat_bubble_background else R.drawable.chat_bubble_background_receiver) // 사용자 메시지와 상대방 메시지의 배경 구분
            setPadding(34, 22, 34, 22)
            setTextColor(resources.getColor(R.color.gray_scale8, null))

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
            //text = time
            //gravity = Gravity.END
            setPadding(8, 4, 8, 8)
            setTextAppearance(R.style.Caption1)
            setTextColor(resources.getColor(R.color.gray_scale7, null))

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                // 시각의 위치를 설정
                if (isUserMessage) {
                    text = time
                    gravity = Gravity.END // 오른쪽 끝에 정렬
                } else {
                    text = time
                    gravity = Gravity.START // 왼쪽 끝에 정렬
                }
            }
            this.layoutParams = layoutParams
        }

        // 채팅 컨테이너에 버블과 시각 추가
        binding.chatContainer.addView(chatBubble)
        binding.chatContainer.addView(timeTextView)

        // 마지막으로 추가된 시간 표시를 추적
        lastTimeTextView = timeTextView
    }

}