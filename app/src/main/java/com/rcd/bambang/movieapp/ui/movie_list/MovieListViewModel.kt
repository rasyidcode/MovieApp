package com.rcd.bambang.movieapp.ui.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

@Suppress("DEPRECATION")
class MovieListViewModel(private val movieListRepository: MovieListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieList: LiveData<PagedList<Movie>> by lazy {
        movieListRepository.fetchMovieList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return movieList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}