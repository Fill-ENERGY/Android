package com.example.energy.presentation.view.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.energy.R
import com.example.energy.data.repository.community.Notification
import com.example.energy.databinding.ItemSingleNotificationBinding
import com.example.energy.databinding.ItemMultipleNotificationsBinding

class NotificationRVAdapter(private var notifications: List<Notification>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SINGLE = 1
        private const val TYPE_MULTIPLE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (notifications[position].isSingle!!) TYPE_SINGLE else TYPE_MULTIPLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_SINGLE) {
            val binding = ItemSingleNotificationBinding.inflate(inflater, parent, false)
            SingleNotificationViewHolder(binding)
        } else {
            val binding = ItemMultipleNotificationsBinding.inflate(inflater, parent, false)
            MultipleNotificationsViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = notifications[position]
        if (holder is SingleNotificationViewHolder) {
            holder.bind(notification)
        } else if (holder is MultipleNotificationsViewHolder) {
            holder.bind(notification)
        }
    }

    override fun getItemCount(): Int = notifications.size

    class SingleNotificationViewHolder(private val binding: ItemSingleNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            // 예시로 직접 이미지 리소스를 설정했습니다. 실제로는 URL을 로드하거나 다른 방식으로 설정할 수 있습니다.
            binding.notificationUserimage.setImageResource(R.drawable.userimage)
            binding.notificationUserName.text = notification.userName
        }
    }

    class MultipleNotificationsViewHolder(private val binding: ItemMultipleNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            // 예시로 직접 이미지 리소스를 설정했습니다. 실제로는 URL을 로드하거나 다른 방식으로 설정할 수 있습니다.
            binding.notificationUserImage1.setImageResource(R.drawable.userimage)
            binding.notificationUserImage2.setImageResource(R.drawable.userimage)
            binding.notificationUserName.text = notification.userName
        }
    }
}
