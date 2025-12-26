package com.sword.cats.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sword.cats.R
import com.sword.cats.presentation.CatsList
import com.sword.cats.presentation.Favourites
import com.sword.cats.presentation.cats_list.CatsListScreen
import com.sword.cats.presentation.favourites.FavouritesScreen
import com.sword.cats.presentation.tabRowScreens

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
                    FavouritesScreen()
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        tabRowScreens.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(when (destination) {
                            CatsList -> R.drawable.ic_pets
                            Favourites -> R.drawable.ic_favourite
                        }),
                        contentDescription = destination.route
                    )
                },
                label = {
                    Text(
                        text = when (destination) {
                            CatsList -> "Cats"
                            Favourites -> "Favourites"
                        }
                    )
                }
            )
        }
    }
}