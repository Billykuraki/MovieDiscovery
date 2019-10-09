package com.billyhsieh.moviediscovery.movies.data.source.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


private const val TMDB_BASE_URL = "http://api.themoviedb.org/3/"

// TODO: store in gradle properties
private const val API_KEY = "64b6f3a69e5717b13ed8a56fe4417e71"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(TMDB_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface TmdbService {

    @GET("discover/movie?")
    fun getMovies(@QueryMap options: Map<String, String>): Call<MoviesDiscoverResponse>

    @GET("movie/{movieid}?api_key=${API_KEY}&language=en-US")
    fun getMovieDetail(@Path("movieid") id: Int): Call<MovieDetailResponse>
}

object TmdbApi {
    val TMDB: TmdbService by lazy { retrofit.create(TmdbService::class.java) }
}