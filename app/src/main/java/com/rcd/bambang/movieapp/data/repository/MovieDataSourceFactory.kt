package com.rcd.bambang.movieapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.rcd.bambang.movieapp.data.api.TheMovieDBInterface
import com.rcd.bambang.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val movieListDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        movieListDataSource.postValue(movieDataSource)
        return movieDataSource
    }

}