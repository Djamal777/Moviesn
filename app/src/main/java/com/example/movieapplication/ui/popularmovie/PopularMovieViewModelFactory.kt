package com.example.movieapplication.ui.popularmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PopularMovieViewModelFactory(private val movieRepository:MoviePagingDataRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularMovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PopularMovieViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}