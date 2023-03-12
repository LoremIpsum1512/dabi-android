package com.dabi.dabi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FeedDetailFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).feedDetailComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_detail, container, false)
    }

}