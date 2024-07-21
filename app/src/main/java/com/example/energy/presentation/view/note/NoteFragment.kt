package com.example.energy.presentation.view.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.databinding.FragmentNoteBinding
import com.example.energy.databinding.FragmentNoteListBinding
import com.example.energy.presentation.view.base.BaseFragment

class NoteFragment : BaseFragment<FragmentNoteBinding>({ FragmentNoteBinding.inflate(it)}) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 테스트 데이터
        val sampleData = listOf(
            NoteItem("김규리", "user123", "그럼 조그만 기다리세요!", "2분 전 "),
            NoteItem("박지민", "user456", "네 감사합니다.!", "어제")

        )


        val noteAdapter = NoteAdapter(sampleData)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
        }






    }


}