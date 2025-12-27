package com.sword.cats.domain.cats_list

import com.sword.cats.data.api.breeds.models.CatBreedDto
import com.sword.cats.data.api.favourites.models.CatFavouriteDto
import com.sword.cats.data.database.CatEntity
import com.sword.cats.presentation.models.CatUiModel

fun List<CatEntity>.toUiModelList() = map { catEntity ->
    catEntity.toUiModel()
}

fun buildCatEntity(catBreedDto: CatBreedDto, catFavouriteDtoList: List<CatFavouriteDto>): CatEntity? {
    val catImageId = catBreedDto.image?.id ?: return null
    val catImageUrl = catBreedDto.image.url
    val catFavouriteDto = catFavouriteDtoList.find { catFavouriteDto ->
        catBreedDto.referenceImageId == catFavouriteDto.imageId
    }
    val (lowerLifeExpectancy, higherLifeExpectancy) = parseLifeExpectancy(catBreedDto.lifeSpan)

    return CatEntity(
        id = catBreedDto.id,
        name = catBreedDto.name,
        description = catBreedDto.description,
        origin = catBreedDto.origin,
        temperament = catBreedDto.temperament,
        imageId = catImageId,
        imageUrl = catImageUrl,
        favouriteId = catFavouriteDto?.id,
        isFavourite = catFavouriteDto != null,
        lowerLifeExpectancy = lowerLifeExpectancy,
        higherLifeExpectancy = higherLifeExpectancy
    )
}

fun CatEntity.toUiModel() =
    CatUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl,
        imageId = imageId,
        favouriteId = favouriteId,
        isFavourite = isFavourite
    )

/**
 * Parses a life span string (e.g., "10 to 13 years") into a pair of lower and upper bounds.
 * @return A Pair where `first` is the lower bound and `second` is the upper bound.
 */
private fun parseLifeExpectancy(lifeSpan: String): Pair<Int?, Int?> {
    val numbers = lifeSpan.split(" ")
        .mapNotNull { it.toIntOrNull() }

    val lower = numbers.getOrNull(0)
    val upper = numbers.getOrNull(1)

    return if (upper == null) {
        Pair(lower, lower)
    } else {
        Pair(lower, upper)
    }
}