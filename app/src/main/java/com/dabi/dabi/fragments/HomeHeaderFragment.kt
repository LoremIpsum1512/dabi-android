package com.dabi.dabi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentHomeFeedFiltersBinding
import com.dabi.dabi.databinding.FragmentHomeHeaderBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HomeHeaderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeHeaderFragment : Fragment() {
    lateinit var binding: FragmentHomeHeaderBinding
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

        return binding.root
    }

}