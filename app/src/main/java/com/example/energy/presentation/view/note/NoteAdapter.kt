package com.example.energy.presentation.view.note

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.data.repository.note.ChatThread
import com.example.energy.databinding.ChatItemBinding
import java.util.Collections

class NoteAdapter(private val noteList: ArrayList<ChatThread>,
                  private val onSwipeClickListener: (ChatThread, Int) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    inner class NoteViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: ChatThread) {

            binding.nameTextView.text = note.name
            binding.userIdTextView.text = note.nickname
            binding.messageTextView.text = note.recentMessage.toString()
            binding.timeTextView.text = note.updatedAt


            //NoteLiveChatActivity로 전환
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, NoteLiveChatActivity::class.java)

                intent.putExtra("Username", note.name)
                intent.putExtra("Id",note.nickname)
                ContextCompat.startActivity(itemView.context, intent, null)
            }










            // 스와이프-> 스와이프 영역 클릭 시, 채팅 목록 삭제
            binding.swipespace.setOnClickListener {
                removeData(this.layoutPosition)
                Toast.makeText(binding.root.context, "삭제했습니다.", Toast.LENGTH_SHORT).show()
                onSwipeClickListener(note, layoutPosition)
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










}