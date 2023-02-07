package com.petapp.capybara.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.petapp.capybara.R
import com.petapp.capybara.databinding.ActivityMainBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_home) as NavHostFragment? ?: return
        NavigationUI.setupWithNavController(viewBinding.bottomNavigation, host.findNavController())
    }

//    MdcTheme {
//        Surface(color = MaterialTheme.colors.background) {
//            val navController = rememberNavController()
//            Scaffold(
//                bottomBar = { BottomNavigation(navController) }
//            ) {
//                NavigationSetup(navController = navController)
//            }
//        }
//    }

    @Composable
    private fun BottomNavigation() {
        androidx.compose.material.BottomNavigation(
            backgroundColor = MaterialTheme.colors.background
        ) {
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_surveys),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.tab_list))
                },
                selected = true,
                onClick = {}
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.tab_calendar))
                },
                selected = false,
                onClick = {}
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_accounts),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.tab_new_profile))
                },
                selected = true,
                onClick = {}
            )
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.tab_settings))
                },
                selected = false,
                onClick = {}
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FeaturesComponentHolder.clearComponent()
    }
}
