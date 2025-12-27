package com.sword.cats.domain.cat_details

import com.sword.cats.data.database.CatEntity
import com.sword.cats.presentation.models.CatDetailsUiModel

fun CatEntity.toUiModel() = CatDetailsUiModel(
    id,
    name,
    imageId,
    imageUrl,
    origin,
    temperament,
    description,
    favouriteId,
    isFavourite
)