package com.rcd.bambang.movieapp.ui.movie_detail

import androidx.lifecycle.LiveData
import com.rcd.bambang.movieapp.data.api.TheMovieDBInterface
import com.rcd.bambang.movieapp.data.repository.MovieDetailsDataSource
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {

    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailsDataSource = MovieDetailsDataSource(apiService, compositeDisposable)
        movieDetailsDataSource.fetchMovieDetails(movieId)

        return movieDetailsDataSource.downloadedMovieDetailResponse
    }

    fun fetchMovieDetailNetworkState() : LiveData<NetworkState> {
        return movieDetailsDataSource.networkState
    }
}