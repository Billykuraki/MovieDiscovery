package com.billyhsieh.moviediscovery.movies.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import com.billyhsieh.moviediscovery.movies.data.source.database.*
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
    private val db: MovieDatabase,
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
                    Timber.d("response size${response.body()?.results.orEmpty().size}")
                    diskIO.execute {
                        db.runInTransaction {
                            db.movieDao().deleteAllMovies()
                            db.movieDao().insert(*response.body()?.asModel().orEmpty())
                        }
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

        val listPage = LivePagedListBuilder(db.movieDao().getMovies(),
            Config(pageSize = TMDB_DICOVERY_PAGE_SIZE, prefetchDistance = 0)
        ).setBoundaryCallback(boundaryCallback)
            .build()

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

    fun getMovieDetail(movieId: Int, callback: (detail: MovieDetail?) -> Unit) {
        network.getMovieDetail(movieId).enqueue(object : Callback<MovieDetailResponse> {
            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                Timber.d("onFailure + ${t.message}")
            }
            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(response.body()?.asDetailModel())
                }
            }
        })
    }

    private fun insertResultIntoDb(result: Response<MoviesDiscoverResponse>) {
        Timber.d("response size${result.body()?.results.orEmpty().size}")
        db.movieDao().insert(*result.body()?.asModel().orEmpty())
    }

}