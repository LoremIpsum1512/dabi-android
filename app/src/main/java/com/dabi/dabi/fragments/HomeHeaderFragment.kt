package com.dabi.dabi.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dabi.dabi.MainActivity
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding
import com.dabi.dabi.di.AppViewModelFactory
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeHeaderFragment : Fragment() {
    lateinit var binding: FragmentHomeHeaderBinding

    @Inject
    lateinit var modelFactory: AppViewModelFactory
    private val viewModel by viewModels<FeedListViewModel> { modelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeHeaderBinding.inflate(
            inflater,
            container,
            false
        )
        binding.filterButton.setOnClickListener {
            val bottomSheet = ModalBottomSheet(viewModel)
            bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
        }
        bindFab(requireContext())
        return binding.root
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    fun bindFab(context: Context) {
        val badgeDrawable = BadgeDrawable.create(context)
        binding.filterButton.viewTreeObserver.addOnGlobalLayoutListener {
            badgeDrawable.horizontalOffset = 42
            badgeDrawable.verticalOffset = 42
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filterCountFlow.distinctUntilChanged().mapLatest {
                    it > 0
                }.collectLatest { filtered ->
                    if (filtered) {
                        BadgeUtils.attachBadgeDrawable(
                            badgeDrawable,
                            binding.filterButton
                        )
                    } else {
                        BadgeUtils.detachBadgeDrawable(
                            badgeDrawable,
                            binding.filterButton
                        )
                    }
                }
            }

        }

    }
}