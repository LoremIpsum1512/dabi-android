package com.dabi.dabi.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dabi.dabi.FeedItemViewHolder
import com.dabi.dabi.HomeHeaderViewHolder
import com.dabi.dabi.R
import com.dabi.dabi.ShowModalEvent
import com.dabi.dabi.databinding.FeedListItemBinding
import com.dabi.dabi.data.Feed
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding

sealed class FeedUIModel {
    class ImageItem(val feed: Feed) : FeedUIModel()

    class HomeHeader(val showModalEvent: ShowModalEvent) : FeedUIModel()
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
        return when (getItem(position)) {
            is FeedUIModel.ImageItem -> R.layout.feed_list_item
            is FeedUIModel.HomeHeader -> R.layout.fragment_home_feed_filters
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
           is FeedUIModel.HomeHeader -> (holder as HomeHeaderViewHolder).bind(uiModel.showModalEvent)
            null -> throw NotImplementedError()
        }

    }
}

class FeedClickEvent(val callback: (feedId: Int) -> Unit) {
    fun onClick(feedId: Int) = callback(feedId)
}