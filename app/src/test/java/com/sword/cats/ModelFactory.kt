package com.sword.cats

import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
import com.sword.cats.data.api.models.CatImageDto
import com.sword.cats.data.api.models.CatWeightDto
import com.sword.cats.data.api.models.FavouriteApiRequest
import com.sword.cats.data.api.models.FavouriteApiResponse
import com.sword.cats.data.database.CatEntity
import com.sword.cats.presentation.models.CatUiModel
import java.time.Instant
import java.util.Date

object ModelFactory {
    const val CAT_ID = "abys"
    const val CAT_NAME = "Abyssinian"
    const val CAT_FAVOURITE_ID = "232413577"
    const val CAT_IMAGE_ID = "0XYvRd7oD"

    fun buildCatDto(): CatDto = CatDto(
        weight = CatWeightDto("7 - 10", "3 - 5"),
        id = CAT_ID,
        name = CAT_NAME,
        temperament = "Active, Energetic",
        origin = "Egypt",
        countryCodes = "EG",
        countryCode = "EG",
        description = "A friendly and active breed",
        lifeSpan = "14 - 15",
        indoor = 1,
        adaptability = 5,
        affectionLevel = 5,
        childFriendly = 4,
        dogFriendly = 4,
        energyLevel = 5,
        grooming = 1,
        healthIssues = 2,
        intelligence = 5,
        sheddingLevel = 2,
        socialNeeds = 4,
        strangerFriendly = 4,
        vocalisation = 3,
        experimental = 0,
        hairless = 0,
        natural = 1,
        rare = 0,
        rex = 0,
        suppressedTail = 0,
        shortLegs = 0,
        hypoallergenic = 0,
        referenceImageId = CAT_IMAGE_ID,
        image = buildCatImageDto()
    )

    fun buildCatImageDto() = CatImageDto(
        id = CAT_IMAGE_ID,
        width = 1204,
        height = 1445,
        url = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
    )

    fun buildCatEntity() = CatEntity(
        id = CAT_ID,
        name = CAT_NAME,
        description = "A friendly and active breed",
        imageId = CAT_IMAGE_ID,
        imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
        favouriteId = CAT_FAVOURITE_ID,
        isFavourite = true,
        origin = "Egypt",
        temperament = "Active, Energetic"
    )

    fun buildCatFavouriteDto() = CatFavouriteDto(
        id = CAT_FAVOURITE_ID,
        userId = "user123",
        imageId = CAT_IMAGE_ID,
        subId = "my-user-1234",
        createdAt = buildUTCDate(),
        image = buildCatImageDto()
    )

    fun buildCatUiModel(isFavourite: Boolean = true, favouriteId: String? = CAT_FAVOURITE_ID) = CatUiModel(
        id = CAT_ID,
        name = CAT_NAME,
        imageId = CAT_IMAGE_ID,
        imageUrl = buildCatImageDto().url,
        favouriteId = favouriteId,
        isFavourite = isFavourite
    )

    fun buildUTCDate(): Date = Date.from(Instant.parse("2023-10-28T17:39:28.000Z"))

    fun buildFavouriteApiRequest() =
        FavouriteApiRequest(imageId = CAT_IMAGE_ID, subId = "my-user-1234")

    fun buildFavouriteApiResponse() =
        FavouriteApiResponse(message = "SUCCESS", id = CAT_FAVOURITE_ID)
}