package com.example.movieapplication.ui.popularmovie

import androidx.lifecycle.LiveData
import androidx.paging.*
import androidx.paging.rxjava3.flowable
import com.example.movieapplication.data.api.POST_PER_PAGE
import com.example.movieapplication.data.repository.MovieDataSource
import com.example.movieapplication.data.repository.NetworkState
import com.example.movieapplication.data.vo.Movie
import io.reactivex.rxjava3.core.Flowable

class MoviePagingDataRepository(private val pagingSource: MovieDataSource) {

    lateinit var moviePagingData:Flowable<PagingData<Movie>>

    fun fetchLiveMoviePagedList():Flowable<PagingData<Movie>>{
        moviePagingData=Pager(
            config= PagingConfig(
                pageSize = POST_PER_PAGE,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = {pagingSource}
            ).flowable
        return moviePagingData
    }

    fun getNetworkState():LiveData<NetworkState>{
        return MovieDataSource.networkState
    }
}