package com.sword.cats.di

import android.content.Context
import com.sword.cats.SwordCatsApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): SwordCatsApplication {
        return app as SwordCatsApplication
    }

    @Provides
    @Singleton
    fun provideContext(application: SwordCatsApplication): Context {
        return application.applicationContext
    }
}