package com.billyhsieh.moviediscovery.movies.data.source.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY release_date DESC")
    fun getMovies(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    fun getMovieById(movieId: String): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg movie: Movie)


}