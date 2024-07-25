package com.example.energy.presentation.view.note

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.databinding.ChatItemBinding

class NoteAdapter(private val noteList: List<NoteItem>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: ChatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteItem) {
            binding.nameTextView.text = note.name
            binding.userIdTextView.text = note.userId
            binding.messageTextView.text = note.message
            binding.timeTextView.text = note.time


            //NoteLiveChatActivity로 전환

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, NoteLiveChatActivity::class.java)

                intent.putExtra("Username", note.name)
                intent.putExtra("Id",note.userId)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
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
}