package com.example.movieapplication.ui.singlemoviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SingleMovieViewModelFactory(private val movieRepository:MovieDetailsRepository,private val movieId:Int):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleMovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SingleMovieViewModel(movieRepository,movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}