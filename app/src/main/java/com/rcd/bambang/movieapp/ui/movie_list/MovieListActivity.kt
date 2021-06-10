package com.rcd.bambang.movieapp.ui.movie_list

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.rcd.bambang.movieapp.R
import com.rcd.bambang.movieapp.data.api.TheMovieDBClient
import com.rcd.bambang.movieapp.data.api.TheMovieDBInterface
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.databinding.ActivityMovieListBinding

class MovieListActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieListViewModel

    lateinit var movieListRepository: MovieListRepository

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMovieListBinding>(
            this@MovieListActivity,
            R.layout.activity_movie_list
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()

        movieListRepository = MovieListRepository(apiService)

        viewModel = getViewModel()

        val movieListAdapter = MovieListAdapter(this@MovieListActivity)
        val gridLayoutManager = GridLayoutManager(this@MovieListActivity, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieListAdapter.getItemViewType(position)
                return if (viewType == movieListAdapter.MOVIE_VIEW_TYPE) 1 // MOVIE_VIEW_TYPE will occupy 1 out of 3
                else 3 // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }

        binding.rvMovieList.layoutManager = gridLayoutManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = movieListAdapter

        viewModel.movieList.observe(this@MovieListActivity, {
            movieListAdapter.submitList(it)
        })

        viewModel.networkState.observe(this@MovieListActivity, {
            binding.pbLoading.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvErrorPopular.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieListAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MovieListViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(movieListRepository) as T
            }

        })[MovieListViewModel::class.java]
    }
}