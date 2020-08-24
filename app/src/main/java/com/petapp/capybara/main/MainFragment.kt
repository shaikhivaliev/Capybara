package com.petapp.capybara.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.petapp.capybara.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val host = childFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment? ?: return
        NavigationUI.setupWithNavController(bottom_navigation, host.findNavController())
    }
}