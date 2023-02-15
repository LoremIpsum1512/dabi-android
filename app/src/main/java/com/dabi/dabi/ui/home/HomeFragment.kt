package com.dabi.dabi.ui.home


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dabi.dabi.MainActivity
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentHomeBinding
import com.dabi.dabi.ui.feed.FeedClickEvent
import com.dabi.dabi.ui.feed.FeedDetailFragment
import com.dabi.dabi.ui.feed.FeedItemDecoration
import com.dabi.dabi.ui.feed.FeedListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomeViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(
            inflater
        )

        bindList(binding)
        return binding.root
    }

    private fun bindList(binding: FragmentHomeBinding) {
        val adapter = FeedListAdapter(
            FeedClickEvent { feedId ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToFeedDetailFragment(
                        feedId
                    )
                )
            }
        )
        binding.feedList.apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(
                context, 2
            )
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.feed_item_spacing)
            this.addItemDecoration(
                FeedItemDecoration(
                    spacingInPixels, spacingInPixels
                )
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.feedFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

    }
}