package com.sword.cats.di

import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.cat_details.CatDetailsRepository
import com.sword.cats.domain.cat_details.CatDetailsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CatDetailsModule {
    @Provides
    fun provideCatDetailsRepository(
        favouritesService: FavouritesService,
        catDao: CatDao
    ): CatDetailsRepository {
        return CatDetailsRepositoryImpl(favouritesService, catDao)
    }
}