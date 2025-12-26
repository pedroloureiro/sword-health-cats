package com.sword.cats.presentation

sealed interface AppDestination { val route: String }

data object CatsList : AppDestination {
    override val route = "cats_list"
}

data object Favourites : AppDestination {
    override val route = "favourites"
}

val tabRowScreens = listOf(CatsList, Favourites)