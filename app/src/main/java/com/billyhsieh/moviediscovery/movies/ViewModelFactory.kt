package com.billyhsieh.moviediscovery.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.billyhsieh.moviediscovery.movies.data.source.MoviesRepository
import com.billyhsieh.moviediscovery.movies.detail.DetailViewModel
import com.billyhsieh.moviediscovery.movies.movies.MoviesViewModel


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val moviesRepository: MoviesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(MoviesViewModel::class.java) ->
                    MoviesViewModel(moviesRepository)
                isAssignableFrom(DetailViewModel::class.java) ->
                    DetailViewModel(moviesRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}