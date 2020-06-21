package com.petapp.capybara.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.R

class AppActivity : AppCompatActivity(R.layout.activity_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment? ?: return
        val navController = host.findNavController()

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navController.navigate(R.id.auth)
        } else {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.nav_graph_main, true).build()
            navController.navigate(R.id.main, null, navOptions)
        }
    }
}
