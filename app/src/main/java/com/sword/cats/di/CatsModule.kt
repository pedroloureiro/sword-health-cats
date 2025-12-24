package com.sword.cats.di

import com.sword.cats.data.api.cats.CatsService
import com.sword.cats.data.api.cats.CatsServiceImpl
import com.sword.cats.data.api.common.ApiServiceFactory
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.cats.CatsProcess
import com.sword.cats.domain.cats.CatsProcessImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CatsModule {
    @Singleton
    @Provides
    fun provideCatsService(apiServiceFactory: ApiServiceFactory): CatsService {
        return CatsServiceImpl(apiServiceFactory.getServiceClient(CatsService.Api::class))
    }

    @Provides
    fun provideCatsProcess(catsService: CatsService, catDao: CatDao): CatsProcess {
        return CatsProcessImpl(catsService, catDao)
    }
}