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
import com.example.energy.databinding.ActivityNoteLiveChatBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteLiveChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteLiveChatBinding
    private var cursor: Int? = 0
    //private var threadId: Int? = null
    private var getUnreadMessage: Int = 0
    private var lastDisplayedDate: String? = null
    private var lastTimeTextView: TextView? = null
    private val allMessages = mutableListOf<GetDetailMessage>() // 모든 메시지를 저장할 리스트

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 threadId, receiverId, username 및 userId 가져오기
        //val threadId: Int? = intent.extras?.getInt("threadId")



        val threadId: Int = intent.extras?.getInt("threadId") ?: 0


        val receiverId = intent.getIntExtra("receiverId", 0)
        val username = intent.getStringExtra("Username") ?: "Unknown User"
        val userId = intent.getStringExtra("Id") ?: "Unknown ID"
        getUnreadMessage = intent.getIntExtra("unreadMessageCount", 0)
        //cursor = intent.getIntExtra("cursor", 0)



        // 메시지 로딩
        if (getUnreadMessage == 0) {

            GetMessages(threadId, cursor, receiverId)

        } else {
            UpdateReadMessages(threadId, cursor, receiverId)
        }

        binding.usernameTextView.text = username
        binding.userIdTextView.text = userId

        // 뒤로 가기 버튼 클릭 시
        binding.backbutton.setOnClickListener {
            finish()
        }

        // 전송 버튼 클릭 시
        binding.sendButton.setOnClickListener {
            val accessToken = getSharedPreferences("userToken", Context.MODE_PRIVATE)
                .getString("accessToken", "none")
            sendMessage(accessToken, threadId, receiverId)




        }

        // 프로필 전환 클릭 리스너
        val clickListener = View.OnClickListener {
            val intent = Intent(this, NoteUserProfileActivity::class.java).apply {
                putExtra("Username", username)
                putExtra("Id", userId)
            }
            startActivity(intent)
        }

        // 프로필 전환 클릭 리스너 적용
        binding.usernameTextView.setOnClickListener(clickListener)
        binding.userIdTextView.setOnClickListener(clickListener)
        binding.UserProfile.setOnClickListener(clickListener)
        binding.showMoreButton.setOnClickListener(clickListener)
    }








    // 쪽지 목록 조회(안 읽은 메시지가 없을 때)
    private fun GetMessages(threadId: Int?, cursor: Int?, receiverId: Int) {
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")

        fun fetchMessages(currentCursor: Int?) {
            NoteRepository.getMessages(accessToken!!, threadId, currentCursor, 10) { response ->
                if (response.result?.messages != null) {
                    // 수신된 메시지를 리스트에 추가하고 역순으로 정렬하여 UI에 표시
                    allMessages.addAll(response.result.messages)
                    displayChatMessages(allMessages.sortedBy { it.createdAt }, receiverId)

                    if (response.result.hasNext == true) {
                        fetchMessages(response.result.nextCursor)
                    }
                } else {
                    Toast.makeText(this, "쪽지 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()


                }
            }
        }

        fetchMessages(cursor)
    }

    // 쪽지 목록 조회 (안 읽은 메시지가 있을 때)
    private fun UpdateReadMessages(threadId: Int?, cursor: Int?, receiverId: Int) {
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")

        fun fetchMessages(currentCursor: Int?) {
            NoteRepository.updateReadMessage(accessToken!!, threadId, currentCursor, 10) { response ->
                if (response.result?.messages != null) {
                    // 수신된 메시지를 리스트에 추가하고 역순으로 정렬하여 UI에 표시
                    allMessages.addAll(response.result.messages)
                    displayChatMessages(allMessages.sortedBy { it.createdAt }, receiverId)

                    if (response.result.hasNext == true) {
                        fetchMessages(response.result.nextCursor)
                    }
                } else {
                    Toast.makeText(this, "쪽지 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()


                }
            }
        }

        fetchMessages(cursor)
    }

    // 메시지를 UI에 표시하는 함수
    private fun displayChatMessages(messages: List<GetDetailMessage>, receiverId: Int) {
        binding.chatContainer.removeAllViews() // 이전 메시지 뷰를 제거

        messages.forEach { message ->
            val dateFormat = formatDate(message.createdAt ?: "")
            val timeFormat = formatTime(message.createdAt ?: "")


            // receiverId와 sender가 같지 않아야 보내는 사람

            val isUserMessage = receiverId != message.sender
            message.content?.let { addChatBubble(it, dateFormat, timeFormat, isUserMessage) }
        }

        // 모든 메시지를 추가한 후 스크롤을 맨 아래로 이동
        binding.chatScrollView.post {
            binding.chatScrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    // 메시지 전송 api
    private fun sendMessage(accessToken: String?, threadId: Int?, receiverId: Int) {
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
        val messageRequest = MessageRequest(threadId!!, message, images, receiverId)



        // 메시지 전송 api 호출

        NoteRepository.sendMessage(accessToken!!, messageRequest) { response ->
            if (response != null) {
                Log.d("NoteLiveChatActivity", "서버 응답: ${response.result}")

                //if (response.result.threadId == null) {
                    //threadId = response.result.threadId


                    val createdAt = response.result.createdAt
                    val dateFormat = formatDate(createdAt)
                    val timeFormat = formatTime(createdAt)

                    addChatBubble(message, dateFormat, timeFormat, true)
                    binding.messageInput.text.clear()
                    binding.chatScrollView.post {
                        binding.chatScrollView.fullScroll(View.FOCUS_DOWN)
                    }

                    cursor = response.result.messageId

                //}

                /*
                val createdAt = response.result.createdAt
                val dateFormat = formatDate(createdAt)
                val timeFormat = formatTime(createdAt)

                addChatBubble(message, dateFormat, timeFormat, true)
                binding.messageInput.text.clear()
                binding.chatScrollView.post {
                    binding.chatScrollView.fullScroll(View.FOCUS_DOWN)
                }

                cursor = response.result.messageId

                 */






            } else {
                Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 시간 형식 변환 함수
    private fun formatDate(createdAt: String): String {
        return try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = simpleDateFormat.parse(createdAt.substring(0, 19)) ?: Date()
            val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            createdAt
        }
    }

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


    //채팅방에 대화 버블 출력

    private fun addChatBubble(message: String, date: String, time: String, isUserMessage: Boolean) {
        // 처음 보낼 때만 날짜 출력
        if (lastDisplayedDate != date) {
            lastDisplayedDate = date
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
            setBackgroundResource(if (isUserMessage) R.drawable.chat_bubble_background else R.drawable.chat_bubble_background_receiver)
            setPadding(34, 22, 34, 22)
            setTextColor(resources.getColor(R.color.gray_scale8, null))
            maxWidth = resources.displayMetrics.densityDpi * 250 / 160
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {

                // 사용자의 메시지와 상대방의 메시지를 구분하여 배치
                if (isUserMessage) {
                    gravity = Gravity.END // 오른쪽 정렬
                    setMargins(50, 30, 8, 8) // 오른쪽 여백
                } else {
                    gravity = Gravity.START // 왼쪽 정렬
                    setMargins(8, 30, 50, 8) // 왼쪽 여백
                }
            }
            this.layoutParams = layoutParams
        }

        // 시간 출력
        val timeTextView = TextView(this).apply {
            text = time
            setPadding(8, 4, 8, 8)
            setTextAppearance(R.style.Caption1)
            setTextColor(resources.getColor(R.color.gray_scale7, null))
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                // 메시지 방향에 따라 시간의 위치를 설정
                gravity = if (isUserMessage) Gravity.END else Gravity.START
            }
            this.layoutParams = layoutParams
        }

        // 채팅 컨테이너에 버블과 시각 추가
        binding.chatContainer.addView(chatBubble)
        binding.chatContainer.addView(timeTextView)

        // 마지막으로 추가된 시간 표시를 추가
        lastTimeTextView = timeTextView
    }
}
