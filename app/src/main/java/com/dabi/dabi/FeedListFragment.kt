package com.dabi.dabi

import FeedListLoadStateAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.dabi.dabi.adapters.FeedListPlaceholderAdapter
import com.dabi.dabi.databinding.FragmentFeedListBinding
import com.dabi.dabi.ui.feed.FeedClickEvent
import com.dabi.dabi.ui.feed.FeedListAdapter
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.dabi.dabi.views.FeedItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: FeedListViewModel by viewModels {
        viewModelFactory
    }

    lateinit var binding: FragmentFeedListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedListBinding.inflate(inflater, container, false)
        container?.let {
            bindList(context = it.context, binding = binding)
            childFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, EmptyFragment())
                .commit()
        }
        return binding.root
    }

    private fun bindList(binding: FragmentFeedListBinding, context: Context) {
        val mainNavController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        val adapter = FeedListAdapter(
            FeedClickEvent { feedId ->
                mainNavController.navigate(
                    FeedDetailFragmentDirections.actionGlobalFeedDetailFragment(
                        feedId
                    )
                )
            }
        )

        val gridLayout = GridLayoutManager(
            context, 2
        )
        val loadStateAdapter = FeedListLoadStateAdapter(retry = adapter::retry)

        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && loadStateAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        binding.feedList.apply {
            this.adapter = adapter.withLoadStateFooter(loadStateAdapter)
            this.layoutManager = gridLayout
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.feed_item_spacing)
            this.addItemDecoration(
                FeedItemDecoration(
                    spacingInPixels, spacingInPixels
                )
            )
        }

        binding.feedListPlaceholder.adapter = FeedListPlaceholderAdapter(
            Array(6) { i -> i }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.feedFlow.collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }
                launch {
                    adapter.loadStateFlow.collectLatest { loadStates ->
                        binding.feedListPlaceholder.isVisible =
                            loadStates.refresh is LoadState.Loading
                        binding.feedList.isVisible = loadStates.refresh is LoadState.NotLoading
                        binding.fragmentContainerView.isVisible =
                            loadStates.refresh is LoadState.Error
                        if (loadStates.refresh is LoadState.Error) {
                            val parcel =
                                EmptyFragmentParcelable.fromException(
                                    (loadStates.refresh as LoadState.Error).error
                                )
                            childFragmentManager.setFragmentResult(
                                EmptyFragment.requestKey,
                                bundleOf(EmptyFragment.argsKey to parcel)
                            )
                        }
                    }
                }
            }
        }

    }

    fun updateStyle() {
        viewModel.applyStyle()
    }
}