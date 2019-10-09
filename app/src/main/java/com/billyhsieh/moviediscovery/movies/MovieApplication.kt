package com.billyhsieh.moviediscovery.movies

import android.app.Application
import com.billyhsieh.moviediscovery.movies.data.source.MoviesRepository
import com.billyhsieh.moviediscovery.movies.data.source.database.MovieDatabase
import com.billyhsieh.moviediscovery.movies.data.source.network.TmdbApi
import timber.log.Timber

class MovieApplication : Application() {

    val movieRepository: MoviesRepository
        get() = MoviesRepository(
            MovieDatabase.getDatabase(this).movieDao(),
            TmdbApi.TMDB
        )

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}