package com.billyhsieh.moviediscovery.movies.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.billyhsieh.moviediscovery.R
import com.billyhsieh.moviediscovery.movies.data.source.database.MovieDetail
import com.billyhsieh.moviediscovery.movies.utils.getPosterUrl
import com.billyhsieh.moviediscovery.movies.utils.getViewModelFactory
import com.billyhsieh.moviediscovery.movies.utils.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*
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
        model.load(args.movieId)
        model.detail.observe(this, Observer {
            updateUI(it)
        })
    }

    private fun updateUI (detail: MovieDetail?) {
        Timber.d(detail.toString())
        loadImage(poster, getPosterUrl(detail?.posterPath))
        title.text = detail?.title
        overview.text = detail?.overview
        genre.text = detail?.genres?.joinToString(separator = "/") {it?.name.orEmpty()}
        release_date.text = detail?.releaseDate
        runtime.text = detail?.runtime
        voteCount.text = detail?.voteCount
        voteAverage.text = detail?.voteAverage
    }


}