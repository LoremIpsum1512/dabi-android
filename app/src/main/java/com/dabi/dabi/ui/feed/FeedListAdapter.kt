package com.dabi.dabi.ui.feed


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FeedListItemBinding
import com.dabi.dabi.model.Feed

class FeedListAdapter(private val clickEvent: FeedClickEvent) :
    PagingDataAdapter<Feed, FeedListAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.pk == newItem.pk
        }

    }

    class ViewHolder(
        private val binding: FeedListItemBinding,
        private val clickEvent: FeedClickEvent
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            binding.feedListItem.setOnClickListener { clickEvent.onClick(feed.pk) }
            binding.apply {
                binding.feed = feed

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<FeedListItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.feed_list_item,
                parent,
                false
            )
        return ViewHolder(binding, clickEvent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = getItem(position)
        if (feed != null)
            holder.bind(feed)
    }
}

class FeedClickEvent(val callback: (feedId: Int) -> Unit) {
    fun onClick(feedId: Int) = callback(feedId)
}