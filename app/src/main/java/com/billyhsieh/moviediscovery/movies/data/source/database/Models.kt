package com.billyhsieh.moviediscovery.movies.data.source.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?
) {

    fun getPosterUrl(): String? {
        return if (posterPath.isNullOrEmpty()) {
            null
        } else {
            "$IMAGE_BASE_URL/${posterPath}"
        }
    }
}

