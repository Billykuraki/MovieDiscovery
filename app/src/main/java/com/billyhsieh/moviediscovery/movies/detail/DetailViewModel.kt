package com.billyhsieh.moviediscovery.movies.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billyhsieh.moviediscovery.movies.data.source.MoviesRepository
import com.billyhsieh.moviediscovery.movies.data.source.database.Movie
import com.billyhsieh.moviediscovery.movies.data.source.network.MovieDetailResponse
import com.billyhsieh.moviediscovery.movies.data.source.network.TmdbApi
import kotlinx.android.synthetic.main.fragment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class DetailViewModel(private val moviesRepository: MoviesRepository): ViewModel() {


    private val _detail = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _detail

    fun start(movieId: String) {

        // repo load detail
        TmdbApi.TMDB.getMovieDetail(movieId.toInt()).enqueue(object : Callback<MovieDetailResponse> {
            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<MovieDetailResponse>,
                response: Response<MovieDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val detail = response.body()
                    Timber.d(detail.toString())

                    title.text = detail?.title
                    overview.text = detail?.overview

                    val sb = StringBuffer()
                    detail?.genres?.forEach {
                        sb.append("/")
                        sb.append(it?.name)
                    }
                    genre.text = sb.toString()
                }
            }
        })




    }

}