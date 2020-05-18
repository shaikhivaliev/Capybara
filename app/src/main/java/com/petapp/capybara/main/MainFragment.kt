package com.petapp.capybara.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petapp.capybara.R
import com.petapp.capybara.navigation.Screens
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject

class MainFragment : Fragment(R.layout.fragment_main),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: MainViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        activity?.let {
            when (item.itemId) {
                R.id.tab_new_profile -> viewModel.navigateTo(Screens.Profiles)
                R.id.tab_calendar -> viewModel.navigateTo(Screens.Calendar)
                R.id.tab_all_survey -> viewModel.navigateTo(Screens.Surveys)
                R.id.tab_types -> viewModel.navigateTo(Screens.Types)
            }
        }
        return true
    }
}