package com.rcd.bambang.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.rcd.bambang.movieapp.data.api.FIRST_PAGE
import com.rcd.bambang.movieapp.data.api.TheMovieDBInterface
import com.rcd.bambang.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

@Suppress("DEPRECATION")
class MovieDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        Log.d(TAG, "params.key : ${params.key}")

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.totalPages >= params.key) {
                        callback.onResult(it.movies, params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        Log.d(TAG, "NetworkState: " + NetworkState.END_OF_LIST)
                        networkState.postValue(NetworkState.END_OF_LIST)
                    }
                }, { t ->
                    networkState.postValue(NetworkState.ERROR)
                    Log.e(TAG, "Subscribe Throws: " + t?.message + ", " + t?.localizedMessage)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {}

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.movies, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e(TAG, "" + it.message)
                })
        )
    }


    companion object {
        const val TAG = "MovieDataSource"
    }
}