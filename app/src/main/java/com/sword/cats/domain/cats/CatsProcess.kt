package com.sword.cats.domain.cats

import com.sword.cats.data.api.cats.CatsService
import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
import com.sword.cats.data.api.models.FavouriteApiRequest
import com.sword.cats.data.api.models.FavouriteApiResponse
import com.sword.cats.data.database.CatDao
import com.sword.cats.data.database.CatEntity
import com.sword.cats.extensions.mapToResult

interface CatsProcess {
    suspend fun search(): Result<List<CatDto>>
    suspend fun save(cats: List<CatEntity>)
    suspend fun searchDb(): Result<List<CatEntity>>
    suspend fun getFavourites(): Result<List<CatFavouriteDto>>
    suspend fun setFavourite(request: FavouriteApiRequest): Result<FavouriteApiResponse>
    suspend fun deleteFavourite(favouriteId: String): Result<Unit>
}

class CatsProcessImpl(
    private val service: CatsService,
    private val catDao: CatDao
) : CatsProcess {
    override suspend fun search(): Result<List<CatDto>> {
        return service.search().mapToResult()
    }

    override suspend fun save(cats: List<CatEntity>) {
        catDao.insertCats(cats)
    }

    override suspend fun searchDb(): Result<List<CatEntity>> {
        return catDao.getAllCats().mapToResult()
    }

    override suspend fun getFavourites(): Result<List<CatFavouriteDto>> {
        return service.getFavourites().mapToResult()
    }

    override suspend fun setFavourite(request: FavouriteApiRequest): Result<FavouriteApiResponse> {
        return service.setFavourite(request).mapToResult()
    }

    override suspend fun deleteFavourite(favouriteId: String): Result<Unit> {
        return service.deleteFavourite(favouriteId).mapToResult()
    }
}