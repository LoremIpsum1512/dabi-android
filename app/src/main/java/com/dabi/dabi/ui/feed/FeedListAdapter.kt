package com.dabi.dabi.ui.feed


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dabi.dabi.databinding.FeedListItemBinding
import com.dabi.dabi.model.Feed

class FeedListAdapter : PagingDataAdapter<Feed, FeedListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.thumbnailImage == newItem.thumbnailImage
        }

    }

    class ViewHolder(private val binding: FeedListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            binding.apply {
                binding.feed = feed
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FeedListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = getItem(position)
        if (feed != null)
            holder.bind(feed)
    }
}
