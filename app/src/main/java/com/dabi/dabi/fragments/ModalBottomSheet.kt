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
import com.dabi.dabi.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ModalBottomSheet(
    private val feedListViewModel: FeedListViewModel
) : BottomSheetDialogFragment() {
    lateinit var binding: ModalBottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ModalBottomSheetBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val feedFilterAdapter = FeedFilterGridAdapter(
            list = StyleType.values().map { it.asFilterEntry() },
        ) { index ->
            val style = StyleType.values()[index]
            feedListViewModel.setStyle(style)
        }
        binding.feedGroupFilter.adapter = feedFilterAdapter
    }


    companion object {
        const val TAG = "ModalBottomSheet"


    }


}