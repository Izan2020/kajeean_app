package com.apple.glantrox.kajeean_app.localdatabase

import androidx.room.Room
import androidx.room.RoomDatabase


@androidx.room.Database (entities = [Favourites::class], version = 3 )
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun FavouriteDAO(): FavouritesDAO


    companion object {
        @Volatile private var INSTANCE: FavouriteDatabase? = null
        fun getInstance(context: android.content.Context): FavouriteDatabase {
            return INSTANCE?: synchronized(this) {
                INSTANCE?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        }
        private fun buildDatabase(context: android.content.Context): FavouriteDatabase {
            return Room.databaseBuilder(context.applicationContext,FavouriteDatabase::class.java,"favourites.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

        }

    }

}