package com.sword.cats.domain.main

import com.sword.cats.data.database.CatEntity
import com.sword.cats.domain.breeds.BreedsProcess

interface MainInteractor {
    suspend fun searchCatBreeds(): Result<List<CatBreedItem>>
}

class MainInteractorImpl(private val breedsProcess: BreedsProcess) : MainInteractor {
    override suspend fun searchCatBreeds(): Result<List<CatBreedItem>> {
        return breedsProcess.search().fold(
            onSuccess = { breedList ->
                val (dbCatsList, uiCatsList) = breedList.map { breed ->
                    Pair(
                        CatEntity(
                            id = breed.id,
                            name = breed.name,
                            origin = breed.origin,
                            temperament = breed.temperament,
                            imageUrl = breed.image?.url,
                            favorite = false,
                            description = breed.description
                        ),
                        CatBreedItem(
                            id = breed.id,
                            name = breed.name,
                            imageUrl = breed.image?.url
                        )
                    )
                }.unzip()
                breedsProcess.save(dbCatsList)
                Result.success(uiCatsList)
            },
            onFailure = {
                breedsProcess.getCatsFromDb().fold(
                    onSuccess = { dbCatList ->
                        val uiCatsList = dbCatList.map { catEntity ->
                            CatBreedItem(
                                id = catEntity.id,
                                name = catEntity.name,
                                imageUrl = catEntity.imageUrl)
                        }
                        Result.success(uiCatsList)
                    },
                    onFailure = {Result.success(emptyList())}
                )
            }
        )
    }
}

data class CatBreedItem(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val favorite: Boolean = false
)