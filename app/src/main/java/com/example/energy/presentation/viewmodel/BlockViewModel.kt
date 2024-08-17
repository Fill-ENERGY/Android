package com.example.energy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energy.data.repository.block.BlockRepository
import com.example.energy.data.repository.block.BlockUserModel
import kotlinx.coroutines.launch

class BlockViewModel: ViewModel() {
    private var _blockList = MutableLiveData<List<BlockUserModel>>()
    fun setBlockList(blockList: List<BlockUserModel>){
        _blockList.value = blockList
    }

//    fun unBlockUser(accessToken: String, blockId: Int) {
//        viewModelScope.launch {
//            BlockRepository.deleteBlockMember(accessToken!!, blockId!!) {
//            }
//        }
//    }

    val getBlockList: LiveData<List<BlockUserModel>>
        get() = _blockList
}