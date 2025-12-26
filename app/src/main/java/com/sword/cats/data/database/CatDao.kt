package com.sword.cats.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCats(cats: List<CatEntity>)

    @Query("SELECT * FROM cats ORDER BY name ASC")
    fun getCatsSortedByNameAsc(): Flow<List<CatEntity>>

    @Query("SELECT * FROM cats WHERE id = :catId")
    suspend fun getCatById(catId: String): CatEntity?

    @Query(
        """
        UPDATE cats
        SET 
            isFavourite = :isFavourite,
            favouriteId = :favouriteId
        WHERE id = :catId
    """
    )
    suspend fun updateFavourite(
        catId: String,
        isFavourite: Boolean,
        favouriteId: String?
    )

    @Query("UPDATE cats SET favouriteId = :favouriteId WHERE id = :catId")
    suspend fun updateFavouriteId(catId: String, favouriteId: String)
}