package com.billyhsieh.moviediscovery.movies.movies


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.billyhsieh.moviediscovery.R
import com.billyhsieh.moviediscovery.movies.data.source.database.Movie
import com.billyhsieh.moviediscovery.movies.data.source.network.NetworkState
import com.billyhsieh.moviediscovery.movies.data.source.network.Status
import com.billyhsieh.moviediscovery.movies.utils.loadImage



class MoviesAdapter(
    private val movieClickListener: MovieClickListener
): PagedListAdapter<Movie, RecyclerView.ViewHolder>(CALLBACK) {


    private var networkState: NetworkState? = null

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.movie_item -> (holder as MovieViewHolder).bind(getItem(position))
            R.layout.loading_item -> (holder as LoadingViewHolder).bindTo(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.loading_item
        } else {
            R.layout.movie_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.movie_item -> MovieViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.movie_item,
                    parent,
                    false
                )
            )
            R.layout.loading_item -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.loading_item,
                parent,
                false
            ))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    inner class MovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.title_text)
        private val overview: TextView = view.findViewById(R.id.overview_text)
        private val poster: ImageView = view.findViewById(R.id.image_poster)

        fun bind(movie: Movie?) {
            val posterUrl = movie?.getPosterUrl()
            loadImage(poster, posterUrl)
            title.text = movie?.title
            overview.text = movie?.overview
            view.setOnClickListener {
                movieClickListener.onClick(movie)
            }
        }
    }

    class LoadingViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val progressBar: ProgressBar = view.findViewById(R.id.progressbar)
        private val errorMsg: TextView = view.findViewById(R.id.errorMsg)

        fun bindTo(networkState: NetworkState?) {
            progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
            errorMsg.visibility = toVisibility(networkState?.msg != null)
            errorMsg.text = networkState?.msg
        }

        private fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

}


interface MovieClickListener {
    /**
     * Called when a movie is clicked
     *
     */
    fun onClick(movie: Movie?)
}