package com.billyhsieh.moviediscovery.movies.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.billyhsieh.moviediscovery.movies.data.source.MoviesRepository


class MoviesViewModel(
    private val repository: MoviesRepository
) : ViewModel() {
    private val liveData = MutableLiveData<String>()
    private val repoResult = map(liveData) {
        repository.getMovies()
    }
    val movies = switchMap(repoResult) { it.pagedList }
    val networkState = switchMap(repoResult) { it.networkState }
    val refreshState = switchMap(repoResult) { it.refreshState }


    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }

    // TODO add search text function?
    fun load(search: String) {
        liveData.value = search
    }

}

