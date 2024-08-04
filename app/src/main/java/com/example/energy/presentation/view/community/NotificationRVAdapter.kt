package com.example.energy.presentation.view.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.Notification
import com.example.energy.databinding.ItemCommentNotificationBinding
import com.example.energy.databinding.ItemLikeNotificationsBinding

class NotificationRVAdapter(private val notifications: ArrayList<Notification>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_COMMENT = 1
        private const val TYPE_LIKE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (notifications[position].isComment!!) TYPE_COMMENT else TYPE_LIKE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_COMMENT) {
            val binding = ItemCommentNotificationBinding.inflate(inflater, parent, false)
            CommentNotificationViewHolder(binding)
        } else {
            val binding = ItemLikeNotificationsBinding.inflate(inflater, parent, false)
            LikeNotificationsViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = notifications[position]
        if (holder is CommentNotificationViewHolder) {
            holder.bind(notification)
//            holder.deleteButton.setOnClickListener {
//                removeData(holder.layoutPosition)
//            }
        } else if (holder is LikeNotificationsViewHolder) {
            holder.bind(notification)
//            holder.deleteButton.setOnClickListener {
//                removeData(holder.layoutPosition)
//            }
        }

    }

    override fun getItemCount(): Int = notifications.size

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        notifications.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class CommentNotificationViewHolder(private val binding: ItemCommentNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val deleteButton: View = itemView.findViewById(R.id.erase_item_view)

        fun bind(notification: Notification) {
            binding.notificationUserimage.setImageResource(R.drawable.userimage)
            binding.notificationUserName.text = notification.userName

            // 삭제 텍스트뷰 클릭시 토스트 표시
            deleteButton.setOnClickListener {
                removeData(this.layoutPosition)
                Toast.makeText(binding.root.context, "삭제했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class LikeNotificationsViewHolder(private val binding: ItemLikeNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val deleteButton: View = itemView.findViewById(R.id.erase_item_view)

        fun bind(notification: Notification) {
            binding.notificationUserImage1.setImageResource(R.drawable.userimage)
            binding.notificationUserImage2.setImageResource(R.drawable.userimage)
            binding.notificationUserName.text = notification.userName

            // 삭제 텍스트뷰 클릭시 토스트 표시
            deleteButton.setOnClickListener {
                removeData(this.layoutPosition)
                Toast.makeText(binding.root.context, "삭제했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

