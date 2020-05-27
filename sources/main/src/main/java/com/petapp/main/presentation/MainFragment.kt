package com.petapp.main.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petapp.main.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main),
    BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottom_navigation.selectedItemId = R.id.tab_profiles
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (!item.isChecked) {
            when (item.itemId) {
                R.id.tab_profiles -> Log.d("tab", "profile")
                R.id.tab_calendar -> Log.d("QQQ", "calendar")
                R.id.tab_surveys -> Log.d("QQQ", "surveys")
            }
        }
        return true
    }
}