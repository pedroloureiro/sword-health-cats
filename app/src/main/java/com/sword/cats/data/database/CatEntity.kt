package com.sword.cats.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val origin: String,
    val temperament: String,
    val imageId: String,
    val imageUrl: String,
    val favouriteId: String?,
    val isFavourite: Boolean,
    val favouriteCreatedAt: Date?
)