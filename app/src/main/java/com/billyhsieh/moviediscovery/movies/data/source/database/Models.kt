package com.billyhsieh.moviediscovery.movies.data.source.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.billyhsieh.moviediscovery.movies.data.source.network.Genre


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
)

data class MovieDetail(
    val title: String,
    val overview: String,
    val popularity: String,
    val posterPath: String?,
    val genres: List<Genre?>,
    val voteAverage: String,
    val voteCount: String,
    val releaseDate: String,
    val runtime: String?
)


