package com.example.energy.presentation.view.note

//import com.example.energy.data.repository.note.NoteRepository.Companion.leaveChatRoom
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.R
import com.example.energy.data.repository.note.NoteRepository
import com.example.energy.databinding.DialogCustomBinding
import com.example.energy.databinding.FragmentNoteBinding
import com.example.energy.presentation.view.base.BaseFragment


class NoteFragment : BaseFragment<FragmentNoteBinding>({ FragmentNoteBinding.inflate(it)}) {


    private lateinit var noteAdapter: NoteAdapter
    private var cursor: String? = null
    private var lastId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setToolBar()



        noteAdapter = NoteAdapter( requireContext(), ArrayList(), {chatThread, position -> })

        //채팅방 목록 조회 api 호출
        loadChatThread()




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


            //다른 채팅 목록을 스와이프 하면 스와이프 해제
            setOnTouchListener { v, event ->
                swipeHelper.removePreviousClamp(this)
                v.performClick()
                false
            }



        }




    }


    private fun loadChatThread() {


        // 토큰 가져오기
        val sharedPreferences = requireActivity().getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences?.getString("accessToken", "none")



        NoteRepository.getChatThreads(accessToken!!, cursor, lastId, 10) { response ->
            if (response != null) {

                Log.d("NoteFragment", "API Response: $response")

                //데이터를 어댑터에 전달
                noteAdapter.updateData(response.result?.threads)

                //cursor, lastId 데이터 값으로 대입
                cursor = response.result?.cursor
                lastId = response.result?.lastId
            } else {
                Toast.makeText(context, "채팅방 목록 불러오기 실패", Toast.LENGTH_LONG).show()
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