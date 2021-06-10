package com.rcd.bambang.movieapp.ui.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.rcd.bambang.movieapp.data.api.PER_PAGE
import com.rcd.bambang.movieapp.data.api.TheMovieDBInterface
import com.rcd.bambang.movieapp.data.repository.MovieDataSource
import com.rcd.bambang.movieapp.data.repository.MovieDataSourceFactory
import com.rcd.bambang.movieapp.data.repository.NetworkState
import com.rcd.bambang.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

@Suppress("DEPRECATION")
class MovieListRepository(private val apiService: TheMovieDBInterface) {

    lateinit var movieList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchMovieList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize = PER_PAGE)
            .build()

        movieList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return movieList
    }

    fun getNetworkState() : LiveData<NetworkState> {
        return Transformations.switchMap(
            moviesDataSourceFactory.movieListDataSource, MovieDataSource::networkState
        )
    }
}