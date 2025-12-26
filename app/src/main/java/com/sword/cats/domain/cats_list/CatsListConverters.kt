package com.sword.cats.domain.cats_list

import com.sword.cats.data.api.breeds.models.CatBreedDto
import com.sword.cats.data.api.favourites.models.CatFavouriteDto
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

fun buildCatEntity(catBreedDto: CatBreedDto, catFavouriteDtoList: List<CatFavouriteDto>): CatEntity? {
    val catImageId = catBreedDto.image?.id ?: return null
    val catImageUrl = catBreedDto.image.url
    val catFavouriteDto = catFavouriteDtoList.find { catFavouriteDto ->
        catBreedDto.referenceImageId == catFavouriteDto.imageId
    }

    return CatEntity(
        id = catBreedDto.id,
        name = catBreedDto.name,
        description = catBreedDto.description,
        origin = catBreedDto.origin,
        temperament = catBreedDto.temperament,
        imageId = catImageId,
        imageUrl = catImageUrl,
        favouriteId = catFavouriteDto?.id,
        isFavourite = catFavouriteDto != null
    )
}