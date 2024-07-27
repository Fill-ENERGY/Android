//package com.example.energy.presentation.view.community
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.energy.R
//import com.example.energy.data.repository.community.CommunityPost
//
//class CommunityFeedAdapter(private val postList: List<CommunityPost>) : RecyclerView.Adapter<CommunityFeedAdapter.CommunityFeedViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityFeedViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_community_feed, parent, false)
//        return CommunityFeedViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CommunityFeedViewHolder, position: Int) {
//        val post = postList[position]
//        holder.bind(post)
//    }
//
//    override fun getItemCount(): Int = postList.size
//
//    inner class CommunityFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val photoRecyclerView: RecyclerView = itemView.findViewById(R.id.item_community_post_image)
//
//        fun bind(post: CommunityPost) {
//            // Other bindings for post such as user profile, title, content, etc.
//
//            if (post.imageUrl.isEmpty()) {
//                photoRecyclerView.visibility = View.GONE
//            } else {
//                photoRecyclerView.visibility = View.VISIBLE
//                photoRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
//                photoRecyclerView.adapter = ItemFeedPhotoAdapter(post.imageUrl)
//            }
//        }
//    }
//}