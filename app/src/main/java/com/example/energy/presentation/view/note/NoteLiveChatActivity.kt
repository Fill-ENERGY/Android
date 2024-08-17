package com.example.energy.presentation.view.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energy.R
import com.example.energy.data.repository.note.MessageRequest
import com.example.energy.data.repository.note.NoteRepository
import com.example.energy.databinding.ActivityNoteLiveChatBinding

class NoteLiveChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteLiveChatBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //토큰 가져오기
        //var sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzODE3OTA5LCJleHAiOjE3MjY0MDk5MDl9.D8cHYgTwnv-k3GdJpSexakAnn7rtZvML1cfkGm9qJoY"

        binding = ActivityNoteLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // username, Id 가져오기

        val username = intent.getStringExtra("Username") ?: "김규리"
        val userId = intent.getStringExtra("Id") ?: "rlarbfl"


        binding.usernameTextView.text = username
        binding.userIdTextView.text = userId


        //뒤로 가기 버튼 클릭 시
        binding.backbutton.setOnClickListener {
            finish()
        }


        //전송 버튼 클릭 시

        binding.sendButton.setOnClickListener {
            sendMessage(accessToken)
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



    private fun sendMessage(accessToken: String?) {

        //val accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imtpaml3aTFAbmF2ZXIuY29tIiwiaWF0IjoxNzIzNzk0MjY1LCJleHAiOjE3MjYzODYyNjV9.I1m8HjK_zT67iTM1rc9RvH57aoCkGjw6pSkaXACZzXA"
        val message = binding.messageInput.text.toString().trim()

        if (message.isEmpty()) {
            Toast.makeText(this, "메시지를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        val messageRequest = MessageRequest(
            2,
            message,
            emptyList(),
            2
        )


        NoteRepository.sendMessage(accessToken!!, messageRequest) { response ->
            if (response != null) {

                addChatBubble(message, true)
                binding.messageInput.text.clear()
                binding.chatScrollView.post {
                    binding.chatScrollView.fullScroll(View.FOCUS_DOWN)
                }
            } else {
                Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun addChatBubble(message: String, isUserMessage: Boolean) {
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

        // 채팅 컨테이너에 버블 추가
        binding.chatContainer.addView(chatBubble)
    }

}