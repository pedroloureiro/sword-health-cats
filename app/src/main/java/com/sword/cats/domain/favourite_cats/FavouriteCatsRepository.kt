package com.sword.cats.domain.favourite_cats

import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.api.favourites.models.CatFavouriteResponse
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.cats_list.toUiModel
import com.sword.cats.presentation.models.CatUiModel
import com.sword.cats.presentation.models.FavouriteCatsUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt

interface FavouriteCatsRepository {
    fun observeCats(): Flow<FavouriteCatsUiModel>
    suspend fun onFavouriteClick(cat: CatUiModel)
}

class FavouriteCatsRepositoryImpl(
    private val favouritesService: FavouritesService,
    private val catDao: CatDao
) : FavouriteCatsRepository {
    override fun observeCats(): Flow<FavouriteCatsUiModel> {
        return catDao.getCatsSortedByNameAsc().map { cats ->
            val favouriteCats = cats.filter { it.isFavourite }
            val favouriteCatsLifeExpectancyList =
                favouriteCats.mapNotNull { it.lowerLifeExpectancy }
            val averageLifeExpectancy = if(favouriteCatsLifeExpectancyList.isNotEmpty()) {
                favouriteCatsLifeExpectancyList.average().roundToInt()
            } else {
                null
            }

            val catUiModels = favouriteCats.map { catEntity ->
                catEntity.toUiModel()
            }

            FavouriteCatsUiModel(
                averageLifeExpectancy = averageLifeExpectancy,
                catsList = catUiModels
            )
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