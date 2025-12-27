package com.sword.cats.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sword.cats.presentation.navigation.CatsList
import com.sword.cats.presentation.navigation.FavouriteCats
import com.sword.cats.presentation.navigation.tabRowScreens

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
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (destination) {
                            CatsList -> Icons.Default.Pets
                            FavouriteCats -> Icons.Default.Favorite
                        },
                        contentDescription = destination.route
                    )
                },
                label = {
                    Text(
                        text = when (destination) {
                            CatsList -> "Cats"
                            FavouriteCats -> "Favourites"
                        }
                    )
                }
            )
        }
    }
}