package com.sword.cats.di

import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.database.CatDao
import com.sword.cats.domain.cats_list.CatsListRepository
import com.sword.cats.domain.cats_list.CatsListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CatsListModule {
    @Provides
    fun provideCatsListRepository(
        breedsService: BreedsService,
        favouritesService: FavouritesService,
        catDao: CatDao
    ): CatsListRepository {
        return CatsListRepositoryImpl(breedsService, favouritesService, catDao)
    }
}