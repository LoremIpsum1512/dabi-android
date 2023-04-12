package com.dabi.dabi

import FeedListLoadStateAdapter
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.dabi.dabi.adapters.FeedListPlaceholderAdapter
import com.dabi.dabi.databinding.FragmentFeedListBinding
import com.dabi.dabi.adapters.FeedClickEvent
import com.dabi.dabi.adapters.FeedListAdapter
import com.dabi.dabi.fragments.FeedDetailFragmentDirections
import com.dabi.dabi.fragments.ModalBottomSheet
import com.dabi.dabi.viewmodels.FeedListViewModel
import kotlinx.coroutines.flow.collect
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
    private lateinit var layoutFactory: FeedListLayoutFactory;

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
                    val layout = HomeFeedListLayoutFactory(
                        getItemViewType = { feedListAdapter.getItemViewType(it) },
                        context = context,
                        event = ShowModalEvent{
                            val bottomSheet = ModalBottomSheet(viewModel)
                            bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
                        }
                    )
                    viewModel.setHeaderUiModel(layout.header!!)
                    binding.feedList.apply {
                        for (i in (0 until this.itemDecorationCount)) {
                            val itemDeco = this.getItemDecorationAt(i)
                            if (itemDeco == layoutFactory.itemDecoration) {
                                this.removeItemDecorationAt(i)
                            }
                        }
                        this.addItemDecoration(layout.itemDecoration)
                        this.layoutManager = layout.layoutManager
                    }
                }
                else -> {

                }
            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedListBinding.inflate(inflater, container, false)
        swipeLayout = binding.swiperefresh
        container?.let {
            layoutFactory = DefaultFeedListLayoutFactory(it.context)
            bindList(context = it.context, binding = binding)
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

    private fun bindList(binding: FragmentFeedListBinding, context: Context) {
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
                        Timber.d("feedFlow")
                        feedListAdapter.submitData(pagingData)
                    }
                }
                launch {
                    feedListAdapter.loadStateFlow.map {
                        it.refresh
                    }.distinctUntilChanged()
                        .collectLatest { loadStates ->
                            Timber.d("loadStateFlow ${loadStates}")
                            if (loadStates is LoadState.Loading) {
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

                            binding.fragmentContainerView.isVisible =
                                loadStates is LoadState.Error
                            if (loadStates is LoadState.Error) {
                                val parcel =
                                    EmptyFragmentParcelable.fromException(
                                        loadStates.error
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

}