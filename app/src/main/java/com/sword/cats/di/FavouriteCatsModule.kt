package com.sword.cats.di

import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.favourite_cats.FavouriteCatsRepository
import com.sword.cats.domain.favourite_cats.FavouriteCatsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FavouriteCatsModule {
    @Provides
    fun provideFavouriteCatsRepository(
        favouritesService: FavouritesService,
        catDao: CatDao
    ): FavouriteCatsRepository {
        return FavouriteCatsRepositoryImpl(favouritesService, catDao)
    }
}