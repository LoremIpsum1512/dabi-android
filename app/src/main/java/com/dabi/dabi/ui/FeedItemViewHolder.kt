package com.dabi.dabi

import androidx.recyclerview.widget.RecyclerView
import com.dabi.dabi.adapters.FeedClickEvent
import com.dabi.dabi.data.Feed
import com.dabi.dabi.databinding.FeedListItemBinding
import com.dabi.dabi.databinding.FragmentHomeFeedFiltersBinding
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding
import com.dabi.dabi.fragments.ModalBottomSheet

class FeedItemViewHolder(
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

class ShowModalEvent(val callback: () -> Unit) {
    fun onClick() = callback()
}

class HomeHeaderViewHolder(
    private val binding: FragmentHomeHeaderBinding,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(showModalEvent :ShowModalEvent) {
        binding.fooBtn.setOnClickListener {
            showModalEvent.onClick()
           /* val bottomSheet = ModalBottomSheet(feedListViewModel)
            bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)*/
        }
    }
}
