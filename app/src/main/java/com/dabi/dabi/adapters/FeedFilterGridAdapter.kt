package com.dabi.dabi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.dabi.dabi.R
import com.dabi.dabi.databinding.ToogleButtonBinding
import timber.log.Timber

class FeedFilterGridAdapter<T>(val list: List<FilterEntry<T>>, val onClick: (index: Int) -> Unit) :
    BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    private var selectedIndex = -1

    override fun getItem(index: Int): FilterEntry<T> {
        return list[index]
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(index: Int, view: View?, parent: ViewGroup): View {
        val binding = ToogleButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val item = getItem(index)

        // binding.isSelected = index == selectedIndex
        if (index == selectedIndex) {
            binding.toggleButton.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.black))
            binding.toggleButton.setTextColor(ContextCompat.getColor(parent.context, R.color.white))
        }else {
            binding.toggleButton.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.bg_gray))
            binding.toggleButton.setTextColor(ContextCompat.getColor(parent.context, R.color.black))
        }
        binding.toggleButton.text = item.name
        binding.toggleButton.setOnClickListener {
            onClick(index)
            onSelect(index)
        }
        return binding.root
    }

    private fun onSelect(index: Int) {
        selectedIndex = index
        notifyDataSetChanged()
    }
}

data class FilterEntry<T>(val name: String, val value: T)