package com.sword.cats.domain.main

import com.sword.cats.data.api.models.FavouriteApiRequest
import com.sword.cats.data.database.CatEntity
import com.sword.cats.domain.cats.CatsProcess
import com.sword.cats.presentation.models.CatUiModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

interface MainInteractor {
    suspend fun search(): Result<List<CatUiModel>>
    suspend fun onFavoriteClick(cat: CatUiModel, isFavourite: Boolean)
}

class MainInteractorImpl(private val catsProcess: CatsProcess) : MainInteractor {
    override suspend fun search(): Result<List<CatUiModel>> = coroutineScope {
        val searchDeferred = async { catsProcess.search() }
        val favouritesDeferred = async { catsProcess.getFavourites() }
        val searchResult = searchDeferred.await()
        val favouritesResult = favouritesDeferred.await()

        if (searchResult.isFailure || favouritesResult.isFailure) {
            return@coroutineScope searchDb()
        }

        val catDtoList = searchResult.getOrNull() ?: emptyList()
        val catFavouriteDtoList = favouritesResult.getOrNull() ?: emptyList()

        val catUiList = catDtoList.mapNotNull { catDto ->
            val catImageId = catDto.image?.id ?: return@mapNotNull null
            val catImageUrl = catDto.image.url

            val catFavouriteDto = catFavouriteDtoList.find { catFavouriteDto ->
                catDto.referenceImageId == catFavouriteDto.imageId
            }

            CatUiModel(
                id = catDto.id,
                name = catDto.name,
                imageId = catImageId,
                imageUrl = catImageUrl,
                favouriteId = catFavouriteDto?.id,
                isFavourite = catFavouriteDto != null)
        }

        val catDbList = catDtoList.mapNotNull { catDto ->
            val catImageId = catDto.image?.id ?: return@mapNotNull null
            val catImageUrl = catDto.image.url
            val catFavouriteDto = catFavouriteDtoList.find { catFavouriteDto ->
                catDto.referenceImageId == catFavouriteDto.imageId
            }

            CatEntity(
                id = catDto.id,
                name = catDto.name,
                description = catDto.description,
                origin = catDto.origin,
                temperament = catDto.temperament,
                imageId = catImageId,
                imageUrl = catImageUrl,
                favouriteId = catFavouriteDto?.id,
                isFavourite = catFavouriteDto != null,
                favouriteCreatedAt = catFavouriteDto?.createdAt
            )
        }

        catsProcess.save(catDbList)
        Result.success(catUiList)
    }

    override suspend fun onFavoriteClick(cat: CatUiModel, isFavourite: Boolean) {
        if(isFavourite) {
            catsProcess.setFavourite(
                FavouriteApiRequest(
                    imageId = cat.imageId,
                    subId = "my-user-1234"
                )
            )
        } else {
            cat.favouriteId?.let {
                catsProcess.deleteFavourite(cat.favouriteId)
            }
        }
    }

    private suspend fun searchDb(): Result<List<CatUiModel>> {
        return catsProcess.searchDb().fold(
            onSuccess = ::handleSearchDbSuccess,
            onFailure = ::handleSearchDbFailure
        )
    }

    private fun handleSearchDbSuccess(dbCatList: List<CatEntity>): Result<List<CatUiModel>> {
        val uiCatList = dbCatList.toUiModelList()
        return Result.success(uiCatList)
    }

    private fun handleSearchDbFailure(throwable: Throwable): Result<List<CatUiModel>> {
        return Result.success(emptyList())
    }
}