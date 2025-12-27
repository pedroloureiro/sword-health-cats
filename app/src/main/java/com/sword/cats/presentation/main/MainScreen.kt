package com.sword.cats.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sword.cats.presentation.components.BottomBar
import com.sword.cats.presentation.navigation.AppNavHost
import com.sword.cats.presentation.navigation.tabRowScreens

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in tabRowScreens.map { it.route }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(navController)
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
