package com.billyhsieh.moviediscovery.movies.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import com.billyhsieh.moviediscovery.movies.data.source.database.Movie
import com.billyhsieh.moviediscovery.movies.data.source.database.MovieBoundaryCallback
import com.billyhsieh.moviediscovery.movies.data.source.database.MovieDao
import com.billyhsieh.moviediscovery.movies.data.source.network.*
import com.billyhsieh.moviediscovery.movies.utils.TMDB_DICOVERY_PAGE_SIZE
import com.billyhsieh.moviediscovery.movies.utils.getDefaultQueryOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MoviesRepository(
    private val movieDao: MovieDao,
    private val network: TmdbService
) {

    val diskIO: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * run network request and insert to database
     */
    private fun refresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        network.getMovies(getDefaultQueryOptions()).enqueue(object :
            Callback<MoviesDiscoverResponse> {
            override fun onFailure(call: Call<MoviesDiscoverResponse>, t: Throwable) {
                networkState.value = NetworkState.error(t.message)
            }
            override fun onResponse(
                call: Call<MoviesDiscoverResponse>,
                response: Response<MoviesDiscoverResponse>
            ) {
                if (response.isSuccessful) {
                    Timber.d("response size" + response.body()?.results.orEmpty().size)
                    diskIO.execute {
                        movieDao.insert(*response.body()?.asDatabaseModel().orEmpty())
                        networkState.postValue(NetworkState.LOADED)
                    }
                }
            }
        })
        return networkState
    }

    fun getMovies(): Listing<Movie> {
        val boundaryCallback = MovieBoundaryCallback(
            handleResponse = this::insertResultIntoDb
        )

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }

        val listPage = LivePagedListBuilder(
            movieDao.getMovies(), Config(
                pageSize = TMDB_DICOVERY_PAGE_SIZE
                , prefetchDistance = 0
            )
        ).setBoundaryCallback(boundaryCallback).build()

        return Listing(
            pagedList = listPage,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    fun getMovieDetail(movieId: Int, callback:(result: MovieDetailResponse) -> Unit) {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        network.getMovieDetail(movieId).enqueue(object : Callback<MovieDetailResponse> {
            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                networkState.value = NetworkState.error(t.message)
            }
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { callback.invoke(it) }
                }
            }
        })
    }

    private fun insertResultIntoDb(result: Response<MoviesDiscoverResponse>) {
        Timber.d("response size" + result.body()?.results.orEmpty().size)
        movieDao.insert(*result.body()?.asDatabaseModel().orEmpty())
    }

}