package com.dabi.dabi.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dabi.dabi.MainActivity
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentHomeFeedFiltersBinding
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding
import com.dabi.dabi.di.AppViewModelFactory
import com.dabi.dabi.viewmodels.FeedListViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [HomeHeaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeHeaderFragment : Fragment() {
    lateinit var binding: FragmentHomeHeaderBinding
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
        binding = FragmentHomeHeaderBinding.inflate(
            inflater,
            container,
            false
        )
        binding.filterButton.setOnClickListener {
            val bottomSheet = ModalBottomSheet(feedListViewModel)
            bottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
        }

        return binding.root
    }

}