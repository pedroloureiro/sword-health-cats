package com.sword.cats.di

import com.sword.cats.data.api.factories.ApiServiceFactory
import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.api.favourites.FavouritesServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FavouritesModule {
    @Singleton
    @Provides
    fun provideFavouritesService(apiServiceFactory: ApiServiceFactory): FavouritesService {
        return FavouritesServiceImpl(apiServiceFactory.getServiceClient(FavouritesService.Api::class))
    }
}