package com.sword.cats.domain.cats

import com.sword.cats.data.api.cats.CatsService
import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.database.CatDao
import com.sword.cats.data.database.CatEntity
import com.sword.cats.extensions.mapToResult

interface CatsProcess {
    suspend fun search(): Result<List<CatDto>>
    suspend fun save(cats: List<CatEntity>)
    suspend fun searchDb(): Result<List<CatEntity>>
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
}