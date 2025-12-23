package com.sword.cats

import com.sword.cats.data.api.breeds.Breed
import com.sword.cats.data.api.breeds.Image
import com.sword.cats.data.api.breeds.Weight
import com.sword.cats.data.database.CatEntity

object ModelFactory {
    fun fakeBreed(
        id: String = "abys",
        name: String = "Abyssinian",
        imageUrl: String? = null
    ): Breed =
        Breed(
            weight = Weight("7 - 10", "3 - 5"),
            id = id,
            name = name,
            temperament = "Active, Energetic",
            origin = "Egypt",
            country_codes = "EG",
            country_code = "EG",
            description = "A friendly and active breed",
            life_span = "14 - 15",
            indoor = 1,
            adaptability = 5,
            affection_level = 5,
            child_friendly = 4,
            dog_friendly = 4,
            energy_level = 5,
            grooming = 1,
            health_issues = 2,
            intelligence = 5,
            shedding_level = 2,
            social_needs = 4,
            stranger_friendly = 4,
            vocalisation = 3,
            experimental = 0,
            hairless = 0,
            natural = 1,
            rare = 0,
            rex = 0,
            suppressed_tail = 0,
            short_legs = 0,
            hypoallergenic = 0,
            image = imageUrl?.let {
                Image(id = "123", width = 12, height = 12, imageUrl)
            }
        )

    fun fakeCatEntity() = CatEntity(
        id = "abys",
        name = "Abyssinian",
        description = "A friendly and active breed",
        imageUrl = "https://image.url/cat.jpg",
        favorite = false,
        origin = "Egypt",
        temperament = "Active, Energetic"
    )
}