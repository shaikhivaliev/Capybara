package com.petapp.capybara.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.petapp.capybara.R

class AppActivity : AppCompatActivity(R.layout.activity_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment? ?: return
        val navController = host.findNavController()

        // todo аутенфикация

    }

}
