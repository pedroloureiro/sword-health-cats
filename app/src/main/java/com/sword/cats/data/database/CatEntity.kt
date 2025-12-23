package com.sword.cats.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: String,
    val name: String,
    val origin: String,
    val temperament: String,
    val imageUrl: String?,
    val favorite: Boolean,
    val description: String
)