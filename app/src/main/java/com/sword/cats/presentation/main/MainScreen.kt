package com.sword.cats.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sword.cats.presentation.CatsList
import com.sword.cats.presentation.Favourites
import com.sword.cats.presentation.cats_list.CatsListScreen
import com.sword.cats.presentation.components.BottomBar
import com.sword.cats.presentation.cats_favourites.CatsFavouritesScreen

@Composable
fun MainScreen() {
    MaterialTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = CatsList.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(CatsList.route) {
                    CatsListScreen()
                }

                composable(Favourites.route) {
                    CatsFavouritesScreen()
                }
            }
        }
    }
}