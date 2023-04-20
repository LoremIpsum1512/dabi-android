package com.dabi.dabi.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.dabi.dabi.MainActivity
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentFeedDetailBinding

class FeedDetailFragment : Fragment() {
    lateinit var binding: FragmentFeedDetailBinding
    private val args by navArgs<FeedDetailFragmentArgs>()
    private lateinit var feedListFragment: FeedListFragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).feedDetailComponent.inject(this)
        feedListFragment = FeedListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedDetailBinding.inflate(inflater, container, false)
        binding.textView.text = args.feedId.toString()
        if (!feedListFragment.isAdded)
            childFragmentManager.beginTransaction()
                .add(R.id.feed_detail_FCV, feedListFragment)
                .commit()

        return binding.root
    }
}

