package com.example.movieapplication.ui.popularmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.data.vo.Movie
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PopularMovieViewModel(private val movieRepository:MoviePagingDataRepository):ViewModel() {

    private val compositeDisposable=CompositeDisposable()
    var dataIsEmpty:Boolean=false

    private val _moviePagingData=MutableLiveData<PagingData<Movie>>()
    val moviePagingData:LiveData<PagingData<Movie>>
        get()=_moviePagingData

    init{
        compositeDisposable.add(
            movieRepository.fetchLiveMoviePagedList()
                .cachedIn(viewModelScope)
                .subscribe{
                    _moviePagingData.value=it
                }
        )
    }

    val networkState:LiveData<NetworkState> by lazy{
        movieRepository.getNetworkState()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

}