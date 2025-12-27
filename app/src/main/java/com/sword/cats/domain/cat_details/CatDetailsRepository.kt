package com.sword.cats.domain.cat_details

import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.api.favourites.models.CatFavouriteResponse
import com.sword.cats.data.database.CatDao
import com.sword.cats.presentation.models.CatDetailsUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CatDetailsRepository {
    fun observeCat(catId: String): Flow<CatDetailsUiModel>
    suspend fun onFavouriteClick(cat: CatDetailsUiModel)
}

class CatDetailsRepositoryImpl(
    private val favouritesService: FavouritesService,
    private val catDao: CatDao
) : CatDetailsRepository {
    override fun observeCat(catId: String): Flow<CatDetailsUiModel> {
        return catDao.getCatByIdFlow(catId).map { it.toUiModel() }
    }

    override suspend fun onFavouriteClick(cat: CatDetailsUiModel) {
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

    private suspend fun unmarkAsFavourite(cat: CatDetailsUiModel) {
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