package com.apple.glantrox.kajeean_app.localdatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDAO {

    @Delete
    suspend fun deleteFavourites(favourites: Favourites)

    @Insert
    suspend fun insertFavourites(favourites: Favourites)

    @Query("SELECT * FROM favourites ORDER BY id ASC")
    fun getAllFavourites(): Flow<List<Favourites>>

    @Query("SELECT * FROM favourites ORDER BY id ASC")
    fun checkFavourites(): List<Favourites>

}