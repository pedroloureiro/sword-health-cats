package com.sword.cats.presentation.navigation

sealed interface AppRoute { val route: String }

sealed interface BottomBarRoute : AppRoute

data object CatsList : BottomBarRoute {
    override val route = "cats_list"
}

data object FavouriteCats : BottomBarRoute {
    override val route = "favourite_cats"
}

val tabRowScreens: List<BottomBarRoute> = listOf(CatsList, FavouriteCats)

data object CatDetails : AppRoute {
    const val ARG_CAT_ID = "catId"
    override val route = "cat_details/{$ARG_CAT_ID}"
    fun createRoute(catId: String) = "cat_details/$catId"
}