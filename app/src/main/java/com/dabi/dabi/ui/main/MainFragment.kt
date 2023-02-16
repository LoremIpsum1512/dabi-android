package com.dabi.dabi.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.dabi.dabi.R
import com.dabi.dabi.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setupBottomNav()
        return binding.root

    }

    private fun setupBottomNav() {
        val nestedNavHostFragment = childFragmentManager.findFragmentById(R.id.bottom_nav_host_fragment)
        val navController = nestedNavHostFragment?.findNavController()
        val bottomNav = binding.bottomNavView
        if (navController != null)
            NavigationUI.setupWithNavController(bottomNav, navController)
    }
}