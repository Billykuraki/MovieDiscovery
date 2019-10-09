package com.billyhsieh.moviediscovery.movies.utils

import androidx.fragment.app.Fragment
import com.billyhsieh.moviediscovery.movies.MovieApplication
import com.billyhsieh.moviediscovery.movies.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as MovieApplication).movieRepository
    return ViewModelFactory(repository)
}