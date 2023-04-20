package com.dabi.dabi.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var feedListFragment: FeedListFragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        feedListFragment = FeedListFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(
            inflater, container, false
        )
        container?.let {
            if (!feedListFragment.isAdded)
                childFragmentManager.beginTransaction()
                    .add(R.id.home_fragment_container_view, feedListFragment)
                    .commit()
        }

        childFragmentManager.setFragmentResult(
            FeedListFragment.feed_list_request_key,
            bundleOf(FeedListFragment.parent_scope_key to FeedListParentScope.Home)
        )

        return binding.root
    }
}