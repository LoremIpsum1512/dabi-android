package com.dabi.dabi.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dabi.dabi.MainActivity
import com.dabi.dabi.databinding.FragmentErrorBinding
import com.dabi.dabi.databinding.FragmentHomeFeedFiltersBinding
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding
import com.dabi.dabi.di.AppViewModelFactory
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.dabi.dabi.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFeedFiltersFragment : Fragment() {
    lateinit var binding: FragmentHomeFeedFiltersBinding


    @Inject
    lateinit var modelFactory: AppViewModelFactory
    private val feedListViewModel by viewModels<FeedListViewModel> { modelFactory }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).homeComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeFeedFiltersBinding.inflate(
            inflater,
            container,
            false
        )

        binding.fooBtn.setOnClickListener {
            val bottomSheet = ModalBottomSheet(feedListViewModel)
            bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
        }
        return binding.root
    }
}