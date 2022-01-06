package com.example.movieapplication.data.api

import com.example.movieapplication.data.vo.MovieDetails
import com.example.movieapplication.data.vo.MovieResponse
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int): Single<MovieDetails>
}