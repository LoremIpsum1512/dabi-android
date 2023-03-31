package com.dabi.dabi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dabi.dabi.R
import com.dabi.dabi.databinding.ToogleButtonBinding
import com.dabi.dabi.viewmodels.ToggleButtonViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ToggleButtonFragment : Fragment() {
    private val viewModel by viewModels<ToggleButtonViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ToogleButtonBinding.inflate(
            inflater,
            container,
            false
        )
        context?.let { context ->
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.styleFlow.collectLatest { style ->
                    binding.toggleButton.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            style.backgroundColor
                        )
                    )

                    binding.toggleButton.setTextColor(
                        ContextCompat.getColor(
                            context,
                            style.textColor
                        )
                    )
                }
            }
        }


        binding.toggleButton.setOnClickListener {
            viewModel.toggle()
        }
        return binding.root
    }
}