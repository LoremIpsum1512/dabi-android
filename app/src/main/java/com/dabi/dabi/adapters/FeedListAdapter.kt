package com.dabi.dabi.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dabi.dabi.*
import com.dabi.dabi.databinding.FeedListItemBinding
import com.dabi.dabi.data.Feed
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding

sealed class FeedUIModel {
    class ImageItem(val feed: Feed) : FeedUIModel()
}

class FeedListAdapter(private val clickEvent: FeedClickEvent) :
    PagingDataAdapter<FeedUIModel, ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<FeedUIModel>() {
        override fun areItemsTheSame(oldItem: FeedUIModel, newItem: FeedUIModel): Boolean {
            if (oldItem is FeedUIModel.ImageItem && newItem is FeedUIModel.ImageItem) {
                return oldItem.feed.pk == newItem.feed.pk
            }
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FeedUIModel, newItem: FeedUIModel): Boolean {
            if (oldItem is FeedUIModel.ImageItem && newItem is FeedUIModel.ImageItem) {
                return oldItem.feed.thumbnailImage == newItem.feed.thumbnailImage
            }
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            null
        }

        return when (item) {
            is FeedUIModel.ImageItem -> R.layout.feed_list_item
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            R.layout.feed_list_item -> {
                val binding =
                    DataBindingUtil.inflate<FeedListItemBinding>(
                        LayoutInflater.from(parent.context),
                        R.layout.feed_list_item,
                        parent,
                        false
                    )
//                val layoutParams = binding.root.layoutParams
//                layoutParams.height = (parent.height * 1).toInt()
//                binding.root.layoutParams = layoutParams
                FeedItemViewHolder(binding, clickEvent)
            }
            R.layout.fragment_home_feed_filters -> {
                val binding = FragmentHomeHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HomeHeaderViewHolder(binding)
            }
            else -> throw UnsupportedOperationException("Unknown viewType")
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is FeedUIModel.ImageItem -> (holder as FeedItemViewHolder).bind(uiModel.feed)
            null -> throw NotImplementedError()
        }

    }


}

class FeedClickEvent(val callback: (feedId: Int) -> Unit) {
    fun onClick(feedId: Int) = callback(feedId)
}