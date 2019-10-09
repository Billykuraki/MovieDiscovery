package com.billyhsieh.moviediscovery.movies.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billyhsieh.moviediscovery.movies.data.source.MoviesRepository
import com.billyhsieh.moviediscovery.movies.data.source.database.MovieDetail

class DetailViewModel(private val moviesRepository: MoviesRepository): ViewModel() {


    private val _detail = MutableLiveData<MovieDetail>()
    val detail: LiveData<MovieDetail> = _detail

    fun load(movieId: Int) {
        moviesRepository.getMovieDetail(movieId) {
            _detail.value = it
        }
    }



}