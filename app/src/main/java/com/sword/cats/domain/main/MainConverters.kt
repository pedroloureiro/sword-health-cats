package com.sword.cats.domain.main

import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.database.CatEntity
import com.sword.cats.presentation.models.CatUiModel

fun List<CatEntity>.toUiModelList() = map {
    CatUiModel(
        id = it.id,
        name = it.name,
        imageUrl = it.imageUrl
    )
}

fun List<CatDto>.toDbAndUiModelPairList() = map { catDto ->
    val dbModel = catDto.toDbModel()
    val uiModel = catDto.toUiModel()
    dbModel to uiModel
}.unzip()

private fun CatDto.toDbModel() = CatEntity(
    id = id,
    name = name,
    origin = origin,
    temperament = temperament,
    imageUrl = image?.url,
    favorite = false,
    description = description
)

private fun CatDto.toUiModel() = CatUiModel(id = id, name = name, imageUrl = image?.url)