package com.sword.cats.di

import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.cats_favourites.CatsFavouritesRepository
import com.sword.cats.domain.cats_favourites.CatsFavouritesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CatsFavouritesModule {
    @Provides
    fun provideCatsFavouritesRepository(
        favouritesService: FavouritesService,
        catDao: CatDao
    ): CatsFavouritesRepository {
        return CatsFavouritesRepositoryImpl(favouritesService, catDao)
    }
}