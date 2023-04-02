package com.dabi.dabi.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dabi.dabi.MainActivity
import com.dabi.dabi.R
import com.dabi.dabi.adapters.FeedFilterGridAdapter
import com.dabi.dabi.data.StyleType
import com.dabi.dabi.databinding.ModalBottomSheetBinding
import com.dabi.dabi.di.AppViewModelFactory
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModalBottomSheet : BottomSheetDialogFragment() {
    @Inject
    lateinit var modelFactory: AppViewModelFactory
    private val feedListViewModel by viewModels<FeedListViewModel> { modelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).homeComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ModalBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )
        val feedFilterAdapter = FeedFilterGridAdapter(
            list = StyleType.values().map { it.asFilterEntry() },
        ) { index ->
            val style =  StyleType.values()[index]
            feedListViewModel.setStyle(style)
        }
        binding.feedGroupFilter.adapter = feedFilterAdapter
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }


}