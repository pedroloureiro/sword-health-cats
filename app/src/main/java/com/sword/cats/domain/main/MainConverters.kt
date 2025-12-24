package com.sword.cats.domain.main

import com.sword.cats.data.database.CatEntity
import com.sword.cats.presentation.models.CatUiModel

fun List<CatEntity>.toUiModelList() = map { catEntity ->
    CatUiModel(
        id = catEntity.id,
        name = catEntity.name,
        imageUrl = catEntity.imageUrl,
        imageId = catEntity.imageId,
        favouriteId = catEntity.favouriteId,
        isFavourite = catEntity.isFavourite
    )
}