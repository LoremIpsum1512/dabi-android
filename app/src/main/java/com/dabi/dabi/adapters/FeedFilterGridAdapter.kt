package com.dabi.dabi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dabi.dabi.databinding.ToogleButtonBinding
import timber.log.Timber

class FeedFilterGridAdapter<T>(val list: List<FilterEntry<T>>, val onClick: (value: T) -> Unit) :
    BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    private var selectedValue: T? = null

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
        binding.toggleButton.text = item.name
        binding.toggleButton.setOnClickListener {
            onClick(item.value)
        }
        Timber.d("getItem $item | $selectedValue | ${item.value == selectedValue}")
        binding.isSelected = item.value == selectedValue
        return binding.root
    }

    fun onSelect(value: T?) {
        Timber.d("Onselect $value")
        selectedValue = value
        notifyDataSetChanged()
    }
}

data class FilterEntry<T>(val name: String, val value: T)