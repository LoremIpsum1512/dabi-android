package com.dabi.dabi


import FeedListLoadStateAdapter
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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.dabi.dabi.databinding.FragmentHomeBinding
import com.dabi.dabi.ui.feed.FeedClickEvent
import com.dabi.dabi.views.FeedItemDecoration
import com.dabi.dabi.ui.feed.FeedListAdapter
import com.dabi.dabi.viewmodels.HomeViewModel
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
            inflater, container, false
        )

        bindList(binding)

        return binding.root
    }

    private fun bindList(binding: FragmentHomeBinding) {
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.feedFlow.collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
                }


            }
        }

    }
}