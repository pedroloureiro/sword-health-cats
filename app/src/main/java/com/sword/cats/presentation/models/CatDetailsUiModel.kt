package com.sword.cats.presentation.models

data class CatDetailsUiModel(
    val id: String,
    val name: String,
    val imageId: String,
    val imageUrl: String,
    val origin: String,
    val temperament: String,
    val description: String,
    val favouriteId: String?,
    val isFavourite: Boolean
)