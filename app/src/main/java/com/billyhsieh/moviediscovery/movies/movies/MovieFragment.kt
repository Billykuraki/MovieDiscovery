package com.billyhsieh.moviediscovery.movies.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.billyhsieh.moviediscovery.R
import com.billyhsieh.moviediscovery.movies.data.source.database.Movie
import com.billyhsieh.moviediscovery.movies.data.source.network.NetworkState
import com.billyhsieh.moviediscovery.movies.utils.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment: Fragment(), MovieClickListener {

    private val model: MoviesViewModel by viewModels { getViewModelFactory() }

    private lateinit var listAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSwipeToRefresh()
        model.load("")
        setHasOptionsMenu(true)
    }

    private fun initAdapter() {
        val adapter = MoviesAdapter(this)
        list.adapter = adapter
        listAdapter = adapter
        model.movies.observe(this, Observer<PagedList<Movie>> {
            listAdapter.submitList(it)
        })
        // observer network state
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

    }

    private fun initSwipeToRefresh() {
        model.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            model.refresh()
        }
    }

    override fun onClick(movie: Movie?) {
        if (movie == null) return
        navigateToMovieDetail(movie.id)
    }

    private fun navigateToMovieDetail(movieId: Int) {
        val action = MovieFragmentDirections.actionMovieToDetailFragment(movieId)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.menu.movie_menu -> showPicker()
        }
        return true
    }

    private fun showPicker() {

    }
}