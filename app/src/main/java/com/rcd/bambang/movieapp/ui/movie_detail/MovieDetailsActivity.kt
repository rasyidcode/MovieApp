package com.rcd.bambang.movieapp.ui.movie_detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rcd.bambang.movieapp.R
import com.rcd.bambang.movieapp.data.api.POSTER_BASE_URL
import com.rcd.bambang.movieapp.data.api.TheMovieDBClient
import com.rcd.bambang.movieapp.data.api.TheMovieDBInterface
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.data.vo.MovieDetails
import com.rcd.bambang.movieapp.databinding.ActivityMovieDetailBinding
import java.text.NumberFormat
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMovieDetailBinding>(
            this@MovieDetailsActivity,
            R.layout.activity_movie_detail
        )
    }

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var repository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Movie Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId = intent.extras?.get("movie_id") as Int
        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        repository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this@MovieDetailsActivity, {
            bindUI(it)
        })

        viewModel.networkState.observe(this@MovieDetailsActivity, {
            binding.pgLoading.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.tvError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it: MovieDetails) {
        binding.tvMovieTitle.text = it.title
        binding.tvMovieTagline.text = it.tagline
        binding.tvMovieReleaseDate.text = it.releaseDate
        binding.tvMovieRating.text = it.rating.toString()
        binding.tvMovieRuntime.text = it.runtime.toString() + " minutes"
        binding.tvMovieOverview.text = it.overview

        val formatCurrency: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        binding.tvMovieBudget.text = formatCurrency.format(it.budget)
        binding.tvMovieRevenue.text = formatCurrency.format(it.revenue)

        val moviePosterUrl: String = POSTER_BASE_URL + it.posterPath
        Glide.with(this@MovieDetailsActivity)
            .load(moviePosterUrl)
            .into(binding.ivMoviePoster)
    }

    private fun getViewModel(movieId: Int): MovieDetailsViewModel {
        return ViewModelProvider(this@MovieDetailsActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieDetailsViewModel(repository, movieId) as T
            }

        })[MovieDetailsViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}