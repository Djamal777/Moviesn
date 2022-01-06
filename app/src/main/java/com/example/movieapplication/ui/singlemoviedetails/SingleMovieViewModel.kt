package com.example.movieapplication.ui.singlemoviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.data.vo.MovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieDetailsRepository: MovieDetailsRepository,movieId:Int):ViewModel() {

    private val compositeDisposable= CompositeDisposable()

    val movieDetails:LiveData<MovieDetails> by lazy{
        movieDetailsRepository.fetchSingleMovieDetails(movieId)
    }

    val networkState:LiveData<NetworkState> by lazy{
        movieDetailsRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}