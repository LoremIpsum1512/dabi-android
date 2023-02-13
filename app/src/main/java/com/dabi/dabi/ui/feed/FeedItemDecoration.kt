package com.dabi.dabi.ui.feed

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FeedItemDecoration(private val verticalSpacing: Int = 8, private val horizontalSpacing: Int = 8) :
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