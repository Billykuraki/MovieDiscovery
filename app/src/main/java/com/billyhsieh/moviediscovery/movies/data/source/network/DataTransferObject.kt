package com.billyhsieh.moviediscovery.movies.data.source.network

import com.billyhsieh.moviediscovery.movies.data.source.database.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



/** {
 *   "results": []
 *  }
 */
data class MoviesDiscoverResponse(val results: List<NetworkMovie>)

@JsonClass(generateAdapter = true)
data class NetworkMovie (
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "poster_path")
    val posterPath: String?
)


// TODO: detail
data class MovieDetailResponse(
    @Json(name = "original_title")
    val title: String,
    val overview: String,
    val popularity: String,
    @Json(name = "poster_path")
    val posterPath: String?,
    val genres: List<Genre?>,
    @Json(name = "vote_average")
    val voteAverage: String,
    @Json(name = "vote_count")
    val voteCount: String,
    @Json(name = "release_date")
    val releaseDate: String,
    val runtime: String
    )

data class Genre(
    val id: Int,
    val name: String)

fun MoviesDiscoverResponse.asDatabaseModel(): Array<Movie> {
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            releaseDate = it.releaseDate,
            posterPath = it.posterPath
        )
    }.toTypedArray()
}