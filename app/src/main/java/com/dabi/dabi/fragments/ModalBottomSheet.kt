package com.dabi.dabi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dabi.dabi.adapters.FeedFilterGridAdapter
import com.dabi.dabi.data.HeightQueryValue
import com.dabi.dabi.data.StyleType
import com.dabi.dabi.databinding.ModalBottomSheetBinding
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
            initialIndex = StyleType.values().indexOf(feedListViewModel.styleType.value),
            list = StyleType.values().map { it.asFilterEntry() },
        ) { index ->
            val style = StyleType.values()[index]
            feedListViewModel.setStyle(style)
        }

        val heights = listOf(
            HeightQueryValue.Below150(),
            HeightQueryValue.Below155(),
            HeightQueryValue.Below160(),
            HeightQueryValue.Below165(),
            HeightQueryValue.Below170(),
            HeightQueryValue.Over171(),
        )

        val heightType = feedListViewModel.heightType.value
        val feedHeightFilterAdapter = FeedFilterGridAdapter(
            initialIndex = heights.indexOfFirst { value -> value.range.first == heightType?.range?.first && value.range.second == heightType.range.second },
            list = heights.map { it.asFilterEntry() }.toList()
        ) { index ->
            val height = heights[index]
            feedListViewModel.setHeight(height)
        }

        binding.feedStyleFilter.adapter = feedFilterAdapter
        binding.feedGroupHeightFilter.adapter = feedHeightFilterAdapter
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }


}