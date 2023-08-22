package com.petapp.capybara

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.navigation.BottomBar
import com.petapp.capybara.navigation.BottomTabs
import com.petapp.capybara.navigation.graphs.RootGraph

@Composable
fun MainScreen() {
    MdcTheme {
        val tabs = remember { BottomTabs.values() }
        val navController = rememberNavController()
        Scaffold(
            backgroundColor = Color.White,
            bottomBar = { BottomBar(navController = navController, tabs = tabs) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                RootGraph(navController = navController)
            }
        }
    }
}
