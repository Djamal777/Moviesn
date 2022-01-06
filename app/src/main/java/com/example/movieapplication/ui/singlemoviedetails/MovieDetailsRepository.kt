package com.example.movieapplication.ui.singlemoviedetails

import androidx.lifecycle.LiveData
import com.example.movieapplication.data.api.TheMovieDBInterface
import com.example.movieapplication.data.repository.MovieDetailsNetworkDataSource
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.data.vo.MovieDetails

class MovieDetailsRepository(private val apiService:TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(movieId:Int):LiveData<MovieDetails>{
        movieDetailsNetworkDataSource= MovieDetailsNetworkDataSource(apiService)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}