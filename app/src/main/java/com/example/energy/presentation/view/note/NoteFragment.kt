package com.example.energy.presentation.view.note

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.getRetrofit
import com.example.energy.data.repository.note.ChatInterface
import com.example.energy.data.repository.note.LeaveChatResponse
import com.example.energy.data.repository.note.NoteRepository.Companion.leaveChatRoom
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.FragmentNoteBinding
import com.example.energy.presentation.view.base.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NoteFragment : BaseFragment<FragmentNoteBinding>({ FragmentNoteBinding.inflate(it)}) {


    private lateinit var noteAdapter: NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setToolBar()

        // 테스트 데이터
        val sampleData = arrayListOf(
            NoteItem("김규리", "user123", "그럼 조그만 기다리세요!", "2분 전 "),
            NoteItem("박지민", "user456", "네 감사합니다.!", "어제")

        )


        // NoteAdapter에 클릭 리스너 추가
        val noteAdapter = NoteAdapter(sampleData) { note, position ->
            // API 호출 시작
            //leaveChatRoom(note.userId.toLong()) {

                // API 호출 성공 시
                //noteAdapter.removeData(position)
                //Toast.makeText(context, "채팅방을 나갔습니다.", Toast.LENGTH_SHORT).show()
            //}
        }







        // 채팅 구분선
        val Itemdeco = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)


        //스와이프 삭제 리사이클러뷰와 연결
        val swipeHelper = SwipeHelper().apply{
            setClamp(200f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)


        //채팅 목록
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteAdapter
            addItemDecoration(Itemdeco)


            //다른 채팅 목록을 클릭하면 스와이프 해제
            setOnTouchListener { v, event ->
                swipeHelper.removePreviousClamp(this)
                v.performClick()
                false
            }






        }







    }



    private fun setToolBar() {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu_chat)
        binding.toolbar.setTitle(R.string.note)
        binding.toolbar.setTitleTextAppearance(requireContext(), R.style.Title1)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.gray_scale8))
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.appbar_search -> {
                    showToast("search")
                    true
                }

                R.id.appbar_sos -> {
                    showToast("sos")
                    showSOSDialog()
                    true
                }

                else -> false
            }
        }
    }

    // API 호출을 통해 채팅방을 나가는 함수
//    private fun leaveChatRoom(threadId: Long, onSuccess: () -> Unit) {
//
//        val token = "토큰값" // 토큰 값
//        val apiService = getRetrofit().create(ChatInterface::class.java)
//
//        //apiService.leaveChatRoom(threadId).enqueue(object : Callback<LeaveChatResponse> {
//            override fun onResponse(call: Call<LeaveChatResponse>, response: Response<LeaveChatResponse>) {
//                if (response.isSuccessful) {
//                    onSuccess()
//                } else {
//                    // 오류 처리
//                    Toast.makeText(context, "채팅방 나가기 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LeaveChatResponse>, t: Throwable) {
//                // 네트워크 오류 처리
//                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }




    //sos 기능
    private fun showSOSDialog() {
        val dialogBinding = DialogCustomBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val window = dialog.window
            val layoutParams = window?.attributes

            // 디바이스 너비의 70%로 설정
            val width = (resources.displayMetrics.widthPixels * 0.7).toInt()

            //radius 적용
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            layoutParams?.width = width
            window?.attributes = layoutParams
        }
        dialog.show()

        dialogBinding.btnDialog.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")

            startActivity(intent)
            dialog.dismiss()
        }

        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
    }



}