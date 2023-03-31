package com.dabi.dabi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dabi.dabi.databinding.ToogleButtonBinding

class FeedFilterGridAdapter(val list: List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(index: Int): String {
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
        binding.toggleButton.text = getItem(index)
        binding.isSelected = true
        return binding.root
    }
}