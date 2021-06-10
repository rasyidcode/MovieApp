package com.rcd.bambang.movieapp.data.api

import com.rcd.bambang.movieapp.data.vo.MovieDetails
import com.rcd.bambang.movieapp.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {
    //https://api.themoviedb.org/3/movie/337404?api_key=86b09f4202d3d95563fc37f7b4636bf2
    //https://api.themoviedb.org/3/movie/popular?api_key=86b09f4202d3d95563fc37f7b4636bf2

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>
}