package com.petapp.capybara.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petapp.capybara.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main),
    BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val host = childFragmentManager.findFragmentById(R.id.nav_host_bottom_menu) as NavHostFragment? ?: return
        navController = host.findNavController()
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.tab_profiles
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_profiles -> navController.navigate(R.id.nav_graph_profiles)
            R.id.tab_calendar -> navController.navigate(R.id.nav_graph_calendar)
            R.id.tab_surveys -> navController.navigate(R.id.nav_graph_surveys)
        }
        return true
    }

}