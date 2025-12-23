package com.sword.cats.di

import com.sword.cats.domain.breeds.BreedsProcess
import com.sword.cats.domain.main.MainInteractor
import com.sword.cats.domain.main.MainInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Provides
    fun provideMainInteractor(breedsProcess: BreedsProcess): MainInteractor {
        return MainInteractorImpl(breedsProcess)
    }
}