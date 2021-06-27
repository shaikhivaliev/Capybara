package com.petapp.capybara.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.petapp.capybara.R
import com.petapp.capybara.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment? ?: return
        NavigationUI.setupWithNavController(viewBinding.bottomNavigation, host.findNavController())
    }
}
