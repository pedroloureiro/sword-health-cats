package com.sword.cats.domain.main

import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.api.favourites.models.CatFavouriteResponse
import com.sword.cats.data.database.CatDao
import com.sword.cats.presentation.models.CatUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MainRepository {
    fun observeCats(): Flow<List<CatUiModel>>
    suspend fun search()
    suspend fun onFavouriteClick(cat: CatUiModel)
}

class MainRepositoryImpl(
    private val breedsService: BreedsService,
    private val favouritesService: FavouritesService,
    private val catDao: CatDao
) :
    MainRepository {
    override fun observeCats(): Flow<List<CatUiModel>> {
        return catDao.getCatsSortedByNameAsc().map { it.toUiModelList() }
    }

    override suspend fun search(): Unit = coroutineScope {
        val searchResponse = async { breedsService.search() }.await()
        val favouritesResponse = async { favouritesService.getFavourites() }.await()

        if (!searchResponse.isSuccessful || !favouritesResponse.isSuccessful) {
            return@coroutineScope
        }
        val catDtoList = searchResponse.body() ?: emptyList()
        val catFavouriteDtoList = favouritesResponse.body() ?: emptyList()

        val updatedCatDbList = catDtoList.mapNotNull { catDto ->
            val currentCat = catDao.getCatById(catId = catDto.id)
            val updatedCat = buildCatEntity(catDto, catFavouriteDtoList)

            when {
                (currentCat == updatedCat) -> null
                (currentCat?.isFavourite == true) && (updatedCat?.isFavourite == false) -> {
                    val favouriteId =
                        favouritesService.markAsFavourite(imageId = updatedCat.imageId).body()?.id
                    updatedCat.copy(isFavourite = true, favouriteId = favouriteId)
                }

                (currentCat?.isFavourite == false) && (updatedCat?.isFavourite == true) -> {
                    updatedCat.favouriteId?.let { favouritesService.unmarkAsFavourite(favouriteId = it) }
                    updatedCat.copy(isFavourite = false, favouriteId = null)
                }

                else -> updatedCat
            }
        }

        if(updatedCatDbList.isNotEmpty()) {
            catDao.insertCats(updatedCatDbList)
        }
    }

    override suspend fun onFavouriteClick(cat: CatUiModel) {
        val newFavouriteValue = !cat.isFavourite
        if (newFavouriteValue) {
            markAsFavourite(cat.id, cat.imageId)
        } else {
            unmarkAsFavourite(cat)
        }
    }

    private suspend fun markAsFavourite(catId: String, catImageId: String) {
        catDao.updateFavourite(
            catId,
            isFavourite = true,
            favouriteId = null
        )
        favouritesService.markAsFavourite(catImageId).body()?.let {
            onMarkAsFavouriteSuccess(catId, response = it)
        }
    }

    private suspend fun onMarkAsFavouriteSuccess(catId: String, response: CatFavouriteResponse) {
        catDao.updateFavouriteId(catId, favouriteId = response.id)
    }

    private suspend fun unmarkAsFavourite(cat: CatUiModel) {
        catDao.updateFavourite(
            cat.id,
            isFavourite = false,
            favouriteId = null
        )
        cat.favouriteId?.let { safeCatFavouriteId ->
            favouritesService.unmarkAsFavourite(safeCatFavouriteId)
        }
    }
}