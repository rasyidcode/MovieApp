package com.rcd.bambang.movieapp.ui.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel(private val movieDetailsRepository: MovieDetailsRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetchMovieDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailsRepository.fetchMovieDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}