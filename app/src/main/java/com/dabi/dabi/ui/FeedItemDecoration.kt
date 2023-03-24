package com.dabi.dabi.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class FeedItemDecoration(
    val verticalSpacing: Int = 8,
    val horizontalSpacing: Int = 8
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = parent.getChildLayoutPosition(view);
        outRect.bottom = verticalSpacing
        val halfHorizontalSpacing = horizontalSpacing / 2
        if (index % 2 == 0) {
            outRect.right = halfHorizontalSpacing
        } else {
            outRect.left = halfHorizontalSpacing
        }
    }
}

class HomeFeedListDecoration(
    verticalSpacing: Int = 8,
    horizontalSpacing: Int = 8
) : FeedItemDecoration(verticalSpacing, horizontalSpacing) {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val index = parent.getChildLayoutPosition(view) - 1
        if (index >= 0) {
            outRect.bottom = verticalSpacing
            val halfHorizontalSpacing = horizontalSpacing / 2
            if (index % 2 == 0) {
                outRect.right = halfHorizontalSpacing
            } else {
                outRect.left = halfHorizontalSpacing
            }

        }
        println()
    }
}