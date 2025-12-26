package com.sword.cats.di

import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.main.MainRepository
import com.sword.cats.domain.main.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Provides
    fun provideMainRepository(
        breedsService: BreedsService,
        favouritesService: FavouritesService,
        catDao: CatDao
    ): MainRepository {
        return MainRepositoryImpl(breedsService, favouritesService, catDao)
    }
}