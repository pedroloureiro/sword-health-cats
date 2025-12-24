package com.sword.cats.presentation.models

data class CatUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val favorite: Boolean = false
)