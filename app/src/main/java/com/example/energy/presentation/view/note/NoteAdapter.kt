package com.example.energy.presentation.view.note

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.note.ChatThread
import com.example.energy.data.repository.note.NoteRepository
import com.example.energy.databinding.ChatItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Collections

class NoteAdapter(
    private val context: Context,
    private val noteList: ArrayList<ChatThread>,
    private val onSwipeClickListener: (ChatThread, Int) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(note: ChatThread) {

            binding.nameTextView.text = note.name
            binding.userIdTextView.text = note.nickname



            // 30자만 미리보기


            binding.messageTextView.apply {
                val content = note.recentMessage?.content ?: ""
                text = if (content.length > 30) {
                    content.substring(0, 30) + "..."
                } else {
                    content
                }
            }



            // 안 읽은 메시지 있다면 카운트 수 출력

            if (note.unreadMessageCount!! > 0) {
                binding.unreadTextView.text = note.unreadMessageCount.toString()
                binding.unreadTextView.visibility = View.VISIBLE

            } else {
                binding.unreadTextView.visibility = View.GONE
            }


            // 날짜 문자열을 LocalDateTime으로 변환

            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val updatedDateTime = LocalDateTime.parse(note.updatedAt, formatter)

            // 현재 시각
            val now = LocalDateTime.now()

            val minutesAgo = ChronoUnit.MINUTES.between(updatedDateTime, now)

            // "몇 분 전" 형식으로 변환
            val timeText = when {
                minutesAgo < 1 -> "방금 전"
                minutesAgo < 60 -> "${minutesAgo}분 전"
                minutesAgo < 1440 -> "${minutesAgo / 60}시간 전"
                else -> "${minutesAgo / 1440}일 전"
            }

            //최종 변환 텍스트 바인딩
            binding.timeTextView.text = timeText


            //NoteLiveChatActivity로 전환
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, NoteLiveChatActivity::class.java)

                intent.putExtra("Username", note.name)
                intent.putExtra("Id",note.nickname)
                intent.putExtra("threadId", note.threadId)
                intent.putExtra("receiverId", note.receiverId)
                intent.putExtra("cursor", note.recentMessage?.messageId ?:0)
                intent.putExtra("unreadMessageCount", note.unreadMessageCount)
                ContextCompat.startActivity(itemView.context, intent, null)
            }






            // 스와이프-> 스와이프 영역 클릭 시, 채팅 목록 삭제
            binding.ExitButton.setOnClickListener {

                if (getClamped()) {  // 스와이프 상태에서만 버튼 클릭이 유효
                    note.threadId?.let { threadId -> leaveChatRoom(threadId) }
                    removeData(this.layoutPosition)
                    Toast.makeText(binding.root.context, "채팅방 나가기", Toast.LENGTH_SHORT).show()
                    onSwipeClickListener(note, layoutPosition)
                }
            }



        }



        fun setClamped(clamped: Boolean) {

            itemView.tag = clamped

        }

        fun getClamped(): Boolean{
            return itemView.tag as? Boolean ?: false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteViewHolder(binding)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])




    }

    override fun getItemCount(): Int = noteList.size




    fun updateData(newList: List<ChatThread>?) {
        noteList.clear()
        if (newList != null) {
            noteList.addAll(newList)
        }
        notifyDataSetChanged()
    }



    // 채팅 item 삭제 함수
    fun removeData(position: Int) {
        noteList.removeAt(position)
        notifyItemRemoved(position)
    }



    private fun leaveChatRoom(threadId: Int)
    {
        // 토큰 가져오기
        val sharedPreferences = context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")

        NoteRepository.leaveChatRoom(accessToken!!, threadId) { response->
            if (response != null) {
                Log.d("채팅방 나가기", "방 나가기 성공")

            } else {
                Log.e("채팅방 나가기","연결 실패")
            }
        }
    }







}