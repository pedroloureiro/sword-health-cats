package com.sword.cats.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sword.cats.presentation.cat_details.CatDetailsScreen
import com.sword.cats.presentation.cats_list.CatsListScreen
import com.sword.cats.presentation.favourite_cats.FavouriteCatsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CatsList.route,
        modifier = modifier
    ) {
        composable(CatsList.route) {
            CatsListScreen(onCatClick = { catId ->
                val route = CatDetails.createRoute(catId)
                navController.navigate(route)
            })
        }

        composable(FavouriteCats.route) {
            FavouriteCatsScreen(onCatClick = { catId ->
                val route = CatDetails.createRoute(catId)
                navController.navigate(route)
            })
        }

        composable(
            route = CatDetails.route,
            arguments = listOf(navArgument(CatDetails.ARG_CAT_ID) { type = NavType.StringType })
        ) {
            CatDetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}
