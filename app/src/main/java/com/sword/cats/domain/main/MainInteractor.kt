package com.sword.cats.domain.main

import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.database.CatEntity
import com.sword.cats.domain.cats.CatsProcess
import com.sword.cats.presentation.models.CatUiModel

interface MainInteractor {
    suspend fun search(): Result<List<CatUiModel>>
}

class MainInteractorImpl(private val catsProcess: CatsProcess) : MainInteractor {
    override suspend fun search(): Result<List<CatUiModel>> {
        return catsProcess.search().fold(
            onSuccess = { catDtoList -> handleSearchSuccess(catDtoList) },
            onFailure = { searchDb() }
        )
    }

    private suspend fun handleSearchSuccess(catDtoList: List<CatDto>): Result<List<CatUiModel>> {
        val (dbCatList, uiCatList) = catDtoList.toDbAndUiModelPairList()
        catsProcess.save(dbCatList)
        return Result.success(uiCatList)
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