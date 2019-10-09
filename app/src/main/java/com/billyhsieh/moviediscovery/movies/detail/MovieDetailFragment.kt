package com.billyhsieh.moviediscovery.movies.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.billyhsieh.moviediscovery.R
import com.billyhsieh.moviediscovery.movies.data.source.network.MovieDetailResponse
import com.billyhsieh.moviediscovery.movies.data.source.network.TmdbApi
import com.billyhsieh.moviediscovery.movies.movies.MoviesViewModel
import com.billyhsieh.moviediscovery.movies.utils.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MovieDetailFragment: Fragment() {


    private val model: DetailViewModel by viewModels { getViewModelFactory() }
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "movie id is " + args.movieId, Toast.LENGTH_SHORT).show()


        TmdbApi.TMDB.getMovieDetail(args.movieId).enqueue(object : Callback<MovieDetailResponse> {
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