package com.dabi.dabi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dabi.dabi.R
import com.dabi.dabi.adapters.FeedFilterGridAdapter
import com.dabi.dabi.data.StyleType
import com.dabi.dabi.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet : BottomSheetDialogFragment() {
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
        binding.feedGroupFilter.adapter = FeedFilterGridAdapter(
            StyleType.values().map { it.value }
        )
        return binding.root
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }


}