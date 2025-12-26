package com.sword.cats.presentation.models

data class CatUiModel(
    val id: String,
    val name: String,
    val imageId: String,
    val imageUrl: String,
    val favouriteId: String?,
    val isFavourite: Boolean
)