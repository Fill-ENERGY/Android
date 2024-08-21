package com.example.energy.presentation.view.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.energy.data.repository.block.BlockRepository
import com.example.energy.data.repository.block.BlockUserModel
import com.example.energy.databinding.ActivityBlockBinding
import com.example.energy.presentation.view.base.BaseActivity
import com.example.energy.presentation.viewmodel.BlockViewModel
import kotlinx.coroutines.launch

class BlockActivity : BaseActivity<ActivityBlockBinding>({ ActivityBlockBinding.inflate(it)}) {
    val blockViewModel : BlockViewModel by viewModels()
    private val blockList = ArrayList<BlockUserModel>()
    private val blockAdapter = BlockAdapter(blockList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //토큰 가져오기
        var sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        var accessToken = sharedPreferences?.getString("accessToken", "none")

        getBlockList(accessToken)

        blockViewModel.getBlockList.observe(this, Observer { blockList ->
            Log.d("blockList", blockList.toString())
            blockAdapter.notifyDataSetChanged()

        })
        blockViewModel.setBlockList(blockList)


        blockAdapter.setItemClickListener(object : BlockAdapter.OnItemClickListener {

            override fun onUnblockClick(blockUserModel: BlockUserModel) {
//                BlockRepository.deleteBlockMember(accessToken!!, blockUserModel.blockId!!){
//                    blockAdapter.notifyDataSetChanged()
//                }
//                blockAdapter.unblockUser(accessToken!!, blockUserModel.blockId!!)
//                blockAdapter.notifyDataSetChanged()
                    //blockViewModel.unBlockUser(accessToken!!, blockUserModel.blockId!!)
                BlockRepository.deleteBlockMember(accessToken!!, blockUserModel.blockId!!) {

                   BlockRepository.getBlockMembers(accessToken!!, 0, 10){
                       response ->
                       if (response?.blocks != null) {
                           blockAdapter.updateUnblock(response.blocks!!)
                       }
                   }
                }

                Log.d("blockList", blockList.toString())
                    blockAdapter.notifyDataSetChanged()

                    //blockAdapter.updateUnblock(blockList)

                showToast("차단 해제되었습니다.")
            }
        })






    }

    private fun getBlockList(accessToken: String?) {
        binding.rvBlock.adapter = blockAdapter
        binding.rvBlock.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        BlockRepository.getBlockMembers(accessToken!!, 0, 10) { response ->
            response?.blocks?.let { blocks ->
                blockList.addAll(blocks)
                blockViewModel.setBlockList(blocks)
                blockViewModel.getBlockList.observe(this, Observer {
                    blockList ->
                    Log.d("blockList", blockList.toString())
                })
                blockAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

}