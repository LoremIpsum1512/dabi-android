package com.dabi.dabi.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dabi.dabi.databinding.FeedListItemPlaceholderBinding

class FeedListPlaceholderAdapter(val list: Array<*>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(index: Int): Any {
        return list[index]!!
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val binding = FeedListItemPlaceholderBinding.inflate(
            LayoutInflater.from(parent!!.context), parent, false
        )
        return binding.root
    }
}