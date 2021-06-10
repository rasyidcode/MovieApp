package com.rcd.bambang.movieapp.ui.movie_list

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rcd.bambang.movieapp.data.api.POSTER_BASE_URL
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.data.vo.Movie
import com.rcd.bambang.movieapp.databinding.ItemMovieListBinding
import com.rcd.bambang.movieapp.databinding.ItemNetworkStateBinding
import com.rcd.bambang.movieapp.ui.movie_detail.MovieDetailsActivity

@Suppress("DEPRECATION")
class MovieListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == MOVIE_VIEW_TYPE) {
            val binding = ItemMovieListBinding.inflate(inflater, parent, false)
            MovieItemViewHolder(binding)
        } else {
            val binding = ItemNetworkStateBinding.inflate(inflater, parent, false)
            NetworkStateViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(private val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie?, context: Context) {
            binding.tvMovieTitle.text = movie?.title
            binding.tvMovieReleaseDate.text = movie?.releaseDate

            val moviePosterUrl = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .into(binding.ivMoviePoster)

            itemView.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("movie_id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateViewHolder(private val binding: ItemNetworkStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.pbNetworkStateLoading.visibility = View.VISIBLE
            } else {
                binding.pbNetworkStateLoading.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                binding.tvErrorNetworkState.visibility = View.VISIBLE
                binding.tvErrorNetworkState.text = networkState.message
            } else if (networkState != null && networkState == NetworkState.END_OF_LIST) {
                binding.tvErrorNetworkState.visibility = View.VISIBLE
                binding.tvErrorNetworkState.text = networkState.message
            } else {
                binding.tvErrorNetworkState.visibility = View.GONE
            }
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                                                      // hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())                             // remove the progressbar at the end
            } else {                                                                // hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())                            // add progress bar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }


}