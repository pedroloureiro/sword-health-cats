package com.sword.cats.di

import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.api.breeds.BreedsServiceImpl
import com.sword.cats.data.api.common.ApiServiceFactory
import com.sword.cats.data.database.CatDao
import com.sword.cats.di.DIConstants.API_AUTHENTICATED
import com.sword.cats.domain.breeds.BreedsProcess
import com.sword.cats.domain.breeds.BreedsProcessImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BreedsModule {
    @Singleton
    @Provides
    fun provideImagesService(@Named(API_AUTHENTICATED) apiServiceFactory: ApiServiceFactory): BreedsService {
        return BreedsServiceImpl(apiServiceFactory.getServiceClient(BreedsService.Api::class))
    }

    @Provides
    fun provideBreedsProcess(breedsService: BreedsService, catDao: CatDao): BreedsProcess {
        return BreedsProcessImpl(breedsService, catDao)
    }
}