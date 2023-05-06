package com.dabi.dabi

import android.R.attr
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.dabi.dabi.adapters.FeedClickEvent
import com.dabi.dabi.data.Feed
import com.dabi.dabi.databinding.FeedListItemBinding


class FeedItemViewHolder(
    private val binding: FeedListItemBinding,
    private val clickEvent: FeedClickEvent,
    private val valueAnimator: ValueAnimator
) :
    RecyclerView.ViewHolder(binding.root) {
    var added = false
    fun bind(feed: Feed) {


        binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        binding.feedListItem.setOnClickListener { clickEvent.onClick(feed.pk) }

        binding.apply {
            binding.feed = feed
        }

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            if (added) return@addOnGlobalLayoutListener
            val width: Int = binding.root.measuredWidth
            val height: Int = binding.root.measuredHeight
            for (pin in feed.pinPositions) {
                val imageView = ImageView(itemView.context)
                imageView.setImageResource(R.drawable.product_partical)
                val px = 6 * binding.root.context.resources.displayMetrics.density

                imageView.translationX = (pin.x * width).toFloat() - px
                imageView.translationY = (pin.y * height).toFloat() - px
                binding.feedListItem.addView(imageView)
                valueAnimator.addUpdateListener { animation ->
                    val progress = animation.animatedValue as Float
                    imageView.alpha = progress
                }
            }

            added = true
            println(width)
        }
    }
}

class ShowModalEvent(val callback: () -> Unit) {
    fun onClick() = callback()
}

//class HomeHeaderViewHolder(
//    private val binding: FragmentHomeHeaderBinding,
//) :
//    RecyclerView.ViewHolder(binding.root) {
//    fun bind(showModalEvent :ShowModalEvent) {
//        binding.fooBtn.setOnClickListener {
//            showModalEvent.onClick()
//        }
//    }
//}
