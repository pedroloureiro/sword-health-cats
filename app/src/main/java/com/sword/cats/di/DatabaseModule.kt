package com.sword.cats.di

import androidx.room.Room
import com.sword.cats.SwordCatsApplication
import com.sword.cats.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: SwordCatsApplication): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "cats-database"
        ).build()
    }

    @Provides
    fun provideCatDao(db: AppDatabase) = db.catDao()
}