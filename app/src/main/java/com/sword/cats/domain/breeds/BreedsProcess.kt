package com.sword.cats.domain.breeds

import com.sword.cats.data.api.breeds.Breed
import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.database.CatDao
import com.sword.cats.data.database.CatEntity
import com.sword.cats.extensions.mapToResult

interface BreedsProcess {
    suspend fun search(): Result<List<Breed>>
    suspend fun save(cats: List<CatEntity>)
    suspend fun getCatsFromDb(): Result<List<CatEntity>>
}

class BreedsProcessImpl(
    private val service: BreedsService,
    private val catDao: CatDao
) : BreedsProcess {
    override suspend fun search(): Result<List<Breed>> {
        return service.search().mapToResult()
    }

    override suspend fun save(cats: List<CatEntity>) {
        catDao.insertCats(cats)
    }

    override suspend fun getCatsFromDb(): Result<List<CatEntity>> {
        return catDao.getAllCats().mapToResult()
    }
}