package com.billyhsieh.moviediscovery.movies.data.source.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {

    companion object {
        fun getDatabase(context: Context): MovieDatabase {
            synchronized(MovieDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MovieDatabase::class.java, "movies").build()
                }
            }
            return INSTANCE
        }
    }


    abstract fun movieDao(): MovieDao
}

private lateinit var INSTANCE: MovieDatabase

