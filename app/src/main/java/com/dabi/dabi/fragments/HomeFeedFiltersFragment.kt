package com.dabi.dabi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dabi.dabi.databinding.FragmentErrorBinding
import com.dabi.dabi.databinding.FragmentHomeFeedFiltersBinding
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding

class HomeFeedFiltersFragment : Fragment() {
    lateinit var binding: FragmentHomeFeedFiltersBinding
    val bottomSheet = ModalBottomSheet()
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

        binding.fooBtn.setOnClickListener { view ->


            bottomSheet.show(childFragmentManager, ModalBottomSheet.TAG)


        }
        return binding.root
    }
}