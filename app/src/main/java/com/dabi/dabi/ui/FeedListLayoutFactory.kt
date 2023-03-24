package com.dabi.dabi

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dabi.dabi.adapters.FeedUIModel
import com.dabi.dabi.ui.FeedItemDecoration
import com.dabi.dabi.ui.HomeFeedListDecoration

abstract class FeedListLayoutFactory {
    // header
    abstract var header: FeedUIModel?

    // GridLayoutManager
    abstract var layoutManager: RecyclerView.LayoutManager

    // FeedItemDecoration
    abstract var itemDecoration: RecyclerView.ItemDecoration
}

class DefaultFeedListLayoutFactory(
    context: Context,
    override var header: FeedUIModel? = null,
    override var itemDecoration: RecyclerView.ItemDecoration = FeedItemDecoration()
) : FeedListLayoutFactory() {
    override var layoutManager: RecyclerView.LayoutManager = GridLayoutManager(
        context, 2
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = 1
        }
    }
}

class HomeFeedListLayoutFactory(
    getItemViewType: (Int) -> Int,
    context: Context,
) : FeedListLayoutFactory() {
    override var header: FeedUIModel? = FeedUIModel.HomeFilterGroup

    override var layoutManager: RecyclerView.LayoutManager = GridLayoutManager(
        context, 2
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (getItemViewType(position)) {
                    R.layout.fragment_home_feed_filters -> 2
                    else -> 1
                }
            }
        }
    }

    override var itemDecoration: RecyclerView.ItemDecoration = HomeFeedListDecoration()

}