package com.billyhsieh.moviediscovery.movies.data.source.database


import androidx.paging.PagedList
import com.billyhsieh.moviediscovery.movies.data.source.network.MoviesDiscoverResponse
import com.billyhsieh.moviediscovery.movies.data.source.network.TmdbApi
import com.billyhsieh.moviediscovery.movies.utils.PagingRequestHelper
import com.billyhsieh.moviediscovery.movies.utils.createStatusLiveData
import com.billyhsieh.moviediscovery.movies.utils.getDefaultQueryOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.Executors


class MovieBoundaryCallback(
    private val handleResponse: (Response<MoviesDiscoverResponse>) -> Unit
): PagedList.BoundaryCallback<Movie>() {

    val helper = PagingRequestHelper(Executors.newCachedThreadPool())
    val networkState = helper.createStatusLiveData()
    val ioExecutor = Executors.newCachedThreadPool()

    var INIT_PAGE = 1

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        Timber.d("onZeroItemsLoaded")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            TmdbApi.TMDB.getMovies(getDefaultQueryOptions())
                .enqueue(createWebserviceCallback(it))
        }
    }

    /**
     * User reached to the end of the list.
     */
    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            val option = getDefaultQueryOptions()
            option["page"] = INIT_PAGE.toString()
            Timber.d(INIT_PAGE.toString())
            TmdbApi.TMDB.getMovies(option)
                .enqueue(createWebserviceCallback(it))
        }
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<MoviesDiscoverResponse> {
        return object : Callback<MoviesDiscoverResponse> {
            override fun onFailure(
                call: Call<MoviesDiscoverResponse>,
                t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<MoviesDiscoverResponse>,
                response: Response<MoviesDiscoverResponse>
            ) {
                insertItemsIntoDb(response, it)
                INIT_PAGE++
            }
        }
    }

    private fun insertItemsIntoDb(
        response: Response<MoviesDiscoverResponse>,
        it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(response)
            it.recordSuccess()
        }
    }

}