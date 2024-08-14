package com.example.energy.presentation.view.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.energy.R
import com.example.energy.data.repository.block.BlockRepository
import com.example.energy.data.repository.block.BlockUserModel
import com.example.energy.databinding.ItemBlockBinding
import com.example.energy.databinding.ItemSearchBinding

class BlockAdapter(private val itemList: MutableList<BlockUserModel>): RecyclerView.Adapter<BlockAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BlockAdapter.ViewHolder {
        val binding: ItemBlockBinding = ItemBlockBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: BlockAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position]) //받아온 데이터를 객체에 넣어주는 작업

        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener {
            itemClickListener.onUnblockClick(itemList[position])
        }

    }

    inner class ViewHolder(val binding: ItemBlockBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(blockUserModel: BlockUserModel) {
            binding.tvNickname.text = blockUserModel.name
            binding.tvEmail.text = blockUserModel.email
            Glide.with(binding.ivProfile.context)
                .load(blockUserModel.profileImg)
                .placeholder(R.drawable.iv_profile)
                .error(R.drawable.iv_profile)
                .into(binding.ivProfile)
        }
    }

    interface OnItemClickListener {
        fun onUnblockClick(blockList: BlockUserModel)
    }

    fun updateUnblock(newBlockList: ArrayList<BlockUserModel>) {
        itemList.clear()
        itemList.addAll(newBlockList)
        this.notifyDataSetChanged()
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }


    private lateinit var itemClickListener : OnItemClickListener


}