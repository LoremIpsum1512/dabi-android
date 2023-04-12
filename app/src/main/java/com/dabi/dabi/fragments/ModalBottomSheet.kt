package com.dabi.dabi.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dabi.dabi.adapters.FeedFilterGridAdapter
import com.dabi.dabi.data.HeightQueryValue
import com.dabi.dabi.data.StyleType
import com.dabi.dabi.data.WeightQueryValue
import com.dabi.dabi.databinding.ModalBottomSheetBinding
import com.dabi.dabi.viewmodels.FeedFilterBottomSheetViewModel
import com.dabi.dabi.viewmodels.FeedListViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomSheet(
    private val feedListViewModel: FeedListViewModel
) : BottomSheetDialogFragment() {
    lateinit var binding: ModalBottomSheetBinding
    private val viewModel by viewModels<FeedFilterBottomSheetViewModel>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.setHeight(feedListViewModel.heightType.value)
        viewModel.setStyle(feedListViewModel.styleType.value)
        viewModel.setWeight(feedListViewModel.weightType.value)
    }

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
            initialIndex = StyleType.values().indexOf(viewModel.selectedStyle.value),
            list = StyleType.values().map { it.asFilterEntry() },
        ) { index ->
            val style = StyleType.values()[index]
            viewModel.setStyle(style)
        }

        val heights = listOf(
            HeightQueryValue.Below150(),
            HeightQueryValue.Below155(),
            HeightQueryValue.Below160(),
            HeightQueryValue.Below165(),
            HeightQueryValue.Below170(),
            HeightQueryValue.Over171(),
        )

        val heightType = viewModel.selectedHeight.value
        val feedHeightFilterAdapter = FeedFilterGridAdapter(
            initialIndex = heights.indexOfFirst { value -> value.range.first == heightType?.range?.first && value.range.second == heightType.range.second },
            list = heights.map { it.asFilterEntry() }.toList()
        ) { index ->
            val height = heights[index]
            viewModel.setHeight(height)
        }


        val weights = listOf(
            WeightQueryValue.Below40(),
            WeightQueryValue.Below48(),
            WeightQueryValue.Below56(),
            WeightQueryValue.Over56()
        )

        val weightType = viewModel.selectedWeight.value
        val feedWeightFilterAdapter = FeedFilterGridAdapter(
            initialIndex = weights.indexOfFirst { value -> value.range.first == weightType?.range?.first && value.range.second == weightType.range.second },
            list = weights.map { it.asFilterEntry() }.toList()
        ) { index ->
            val weight = weights[index]
            viewModel.setWeight(weight)
        }

        binding.feedStyleFilter.adapter = feedFilterAdapter
        binding.feedGroupHeightFilter.adapter = feedHeightFilterAdapter
        binding.feedGroupWeightFilter.adapter = feedWeightFilterAdapter

        binding.applyFilterButton.setOnClickListener { _ ->
            viewModel.selectedStyle.value?.let { it -> feedListViewModel.setStyle(it) }
            viewModel.selectedHeight.value?.let { it -> feedListViewModel.setHeight(it) }
            viewModel.selectedWeight.value?.let { it -> feedListViewModel.setWeight(it) }
            this.dismiss()
        }
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }


}