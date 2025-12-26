package com.sword.cats.domain.cats_favourites

import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.api.favourites.models.CatFavouriteResponse
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.cats_list.toUiModelList
import com.sword.cats.presentation.models.CatUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CatsFavouritesRepository {
    fun observeCats(): Flow<List<CatUiModel>>
    suspend fun onFavouriteClick(cat: CatUiModel)
}

class CatsFavouritesRepositoryImpl(
    private val favouritesService: FavouritesService,
    private val catDao: CatDao
) : CatsFavouritesRepository {
    override fun observeCats(): Flow<List<CatUiModel>> {
        return catDao.getCatsSortedByNameAsc().map { it.toUiModelList() }
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