package com.sword.cats.di

import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.api.breeds.BreedsServiceImpl
import com.sword.cats.data.api.factories.ApiServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BreedsModule {
    @Singleton
    @Provides
    fun provideBreedsService(apiServiceFactory: ApiServiceFactory): BreedsService {
        return BreedsServiceImpl(apiServiceFactory.getServiceClient(BreedsService.Api::class))
    }
}