package com.sword.cats.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sword.cats.presentation.CatsList
import com.sword.cats.presentation.Favourites
import com.sword.cats.presentation.cat_details.CatDetailsScreen
import com.sword.cats.presentation.cats_favourites.CatsFavouritesScreen
import com.sword.cats.presentation.cats_list.CatsListScreen
import com.sword.cats.presentation.components.BottomBar

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
                    CatsListScreen(navController)
                }

                composable(Favourites.route) {
                    CatsFavouritesScreen(navController)
                }

                composable(
                    route = "cat_detail/{catId}",
                    arguments = listOf(navArgument("catId") { type = NavType.StringType })
                ) {
                    CatDetailsScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}