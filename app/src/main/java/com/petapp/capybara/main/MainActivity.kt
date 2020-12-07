package com.petapp.capybara.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.petapp.capybara.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment? ?: return
        NavigationUI.setupWithNavController(bottom_navigation, host.findNavController())
    }
}
