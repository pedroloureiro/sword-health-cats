package com.sword.cats.presentation

sealed interface AppRoute { val route: String }

sealed interface BottomBarRoute : AppRoute

data object CatsList : BottomBarRoute {
    override val route = "cats_list"
}

data object Favourites : BottomBarRoute {
    override val route = "favourites"
}

val tabRowScreens: List<BottomBarRoute> = listOf(CatsList, Favourites)

data object CatDetails : AppRoute {
    override val route = "cat_detail/{catId}"
    fun createRoute(catId: String) = "cat_detail/$catId"
}