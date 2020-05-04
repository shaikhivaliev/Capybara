package com.petapp.capybara.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petapp.capybara.R
import com.petapp.capybara.Screens
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main),
    BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        activity?.let {
            when (item.itemId) {
                R.id.tab_new_profile -> Screens.navigateTo(
                    it,
                    Screens.NewProfile,
                    R.id.main_container
                )
                R.id.tab_calendar -> Screens.navigateTo(
                    it,
                    Screens.Calendar,
                    R.id.main_container
                )
                R.id.tab_all_survey -> Screens.navigateTo(
                    it,
                    Screens.AllSurvey,
                    R.id.main_container
                )
                R.id.tab_types -> Screens.navigateTo(
                    it,
                    Screens.Types,
                    R.id.main_container
                )
            }
        }
        return true
    }
}