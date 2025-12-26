package com.sword.cats.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CatEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
}