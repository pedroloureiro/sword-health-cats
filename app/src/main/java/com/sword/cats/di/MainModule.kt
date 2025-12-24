package com.sword.cats.di

import com.sword.cats.domain.cats.CatsProcess
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
    fun provideMainInteractor(catsProcess: CatsProcess): MainInteractor {
        return MainInteractorImpl(catsProcess)
    }
}