package com.example.movieapplication.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.movieapplication.data.api.FIRST_PAGE
import com.example.movieapplication.data.api.TheMovieDBInterface
import com.example.movieapplication.data.vo.Movie
import com.example.movieapplication.data.vo.MovieResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDataSource(private val apiService: TheMovieDBInterface) : RxPagingSource<Int, Movie>() {

    private var page = FIRST_PAGE
    companion object {
        val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val position=params.key?:page
        networkState.postValue(NetworkState.LOADING)
        return apiService.getPopularMovie(position)
            .subscribeOn(Schedulers.io())
            .map{toLoadResult(it, position)}
            .doOnSuccess{networkState.postValue(NetworkState.LOADED)}
            .onErrorReturn{
                networkState.postValue(NetworkState.ERROR)
                LoadResult.Error(it)
            }
    }

    private fun toLoadResult(data:MovieResponse, position:Int):LoadResult<Int,Movie>{
        return LoadResult.Page(
            data=data.movieList,
            prevKey = if(position==1) null else position-1,
            nextKey = if(position==data.totalPages) null else data.page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}