package com.dabi.dabi.fragments

import FeedListLoadStateAdapter
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dabi.dabi.*
import com.dabi.dabi.adapters.FeedListPlaceholderAdapter
import com.dabi.dabi.databinding.FragmentFeedListBinding
import com.dabi.dabi.adapters.FeedClickEvent
import com.dabi.dabi.adapters.FeedListAdapter
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class FeedListParentScope(val value: String) {
    Home("home"),
    Self("self")
}

class FeedListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: FeedListViewModel
    lateinit var feedListAdapter: FeedListAdapter
    lateinit var binding: FragmentFeedListBinding
    lateinit var swipeLayout: SwipeRefreshLayout
    private var parentScope: FeedListParentScope = FeedListParentScope.Self
    lateinit var layoutFactory: FeedListLayoutFactory
    lateinit var badgeDrawable: BadgeDrawable
    private var initialFetched = false;

    companion object {
        const val feed_list_request_key = "feedListRequestKey"
        const val parent_scope_key = "parent_scope_key"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).feedListComponent.inject(this)
        viewModel = viewModelFactory.create(FeedListViewModel::class.java)

        setFragmentResultListener(feed_list_request_key) { _, bundle ->
            parentScope = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable(parent_scope_key, FeedListParentScope::class.java)!!
            } else {
                @Suppress("DEPRECATION") bundle.getSerializable(parent_scope_key) as FeedListParentScope
            }
            when (parentScope) {

                FeedListParentScope.Home -> {
                    (activity as MainActivity).homeComponent.inject(this)
                    viewModel = viewModelFactory.create(FeedListViewModel::class.java)
                    // bindFab(context)
                }
                else -> {}
            }
        }
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedListBinding.inflate(inflater, container, false)
        swipeLayout = binding.swiperefresh
        container?.let {
            layoutFactory = DefaultFeedListLayoutFactory(it.context)
            bindList(binding = binding)
            childFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, EmptyFragment())
                .commit()
            swipeLayout.setOnRefreshListener {
                viewModel.refresh()
                feedListAdapter.refresh()

            }
        }
        return binding.root
    }

    private fun bindList(binding: FragmentFeedListBinding) {
        val mainNavController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        feedListAdapter = FeedListAdapter(
            FeedClickEvent { feedId ->
                mainNavController.navigate(
                    FeedDetailFragmentDirections.actionGlobalFeedDetailFragment(
                        feedId
                    )
                )
            }
        )

        val loadStateAdapter = FeedListLoadStateAdapter(retry = feedListAdapter::retry)

        binding.feedList.apply {
            this.adapter = feedListAdapter.withLoadStateFooter(loadStateAdapter)
            this.layoutManager = layoutFactory.layoutManager
            this.addItemDecoration(
                layoutFactory.itemDecoration
            )

        }

        binding.feedListPlaceholder.adapter = FeedListPlaceholderAdapter(
            Array(6) { i -> i }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiModelFlow.collectLatest { pagingData ->
                        Timber.d("uiModelFlow")
                        feedListAdapter.submitData(pagingData)
                    }
                }
                launch {
                    feedListAdapter.loadStateFlow.distinctUntilChanged()
                        .collectLatest { combinedLoadStates ->
                            Timber.d("loadStateFlow $combinedLoadStates")
                            val refreshState = combinedLoadStates.refresh

                            if (refreshState is LoadState.Loading) {
                                if (feedListAdapter.itemCount <= 0)
                                    binding.feedListPlaceholder.isVisible =
                                        true
                                else {
                                    swipeLayout.isRefreshing = true
                                }
                            } else {
                                binding.feedListPlaceholder.isVisible = false
                                if (swipeLayout.isRefreshing) swipeLayout.isRefreshing = false
                            }

                            binding.fragmentContainerView.isVisible = false

                            if (refreshState is LoadState.Error) {
                                binding.fragmentContainerView.isVisible = true
                                val parcel =
                                    EmptyFragmentParcelable.fromException(
                                        throwable = refreshState.error
                                    ) {
                                        viewModel.refresh()
                                    }
                                childFragmentManager.setFragmentResult(
                                    EmptyFragment.requestKey,
                                    bundleOf(EmptyFragment.argsKey to parcel)
                                )
                            }

                            if (
                                initialFetched &&
                                refreshState is LoadState.NotLoading
                                && combinedLoadStates.append.endOfPaginationReached
                                && feedListAdapter.itemCount <= 0
                            ) {
                                binding.fragmentContainerView.isVisible = true
                                val parcel = EmptyFragmentParcelable(
                                    title = "Không tìm thấy bài viết",
                                    imageResource = R.drawable.info_post
                                )
                                childFragmentManager.setFragmentResult(
                                    EmptyFragment.requestKey,
                                    bundleOf(EmptyFragment.argsKey to parcel)
                                )
                            } else {
                                initialFetched = true
                            }
                        }
                }
            }
        }
    }


}