package com.sword.cats.domain.main

import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
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

fun buildCatEntity(catDto: CatDto, catFavouriteDtoList: List<CatFavouriteDto>): CatEntity? {
    val catImageId = catDto.image?.id ?: return null
    val catImageUrl = catDto.image.url
    val catFavouriteDto = catFavouriteDtoList.find { catFavouriteDto ->
        catDto.referenceImageId == catFavouriteDto.imageId
    }

    return CatEntity(
        id = catDto.id,
        name = catDto.name,
        description = catDto.description,
        origin = catDto.origin,
        temperament = catDto.temperament,
        imageId = catImageId,
        imageUrl = catImageUrl,
        favouriteId = catFavouriteDto?.id,
        isFavourite = catFavouriteDto != null
    )
}