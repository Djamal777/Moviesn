package com.example.movieapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.movieapplication.data.api.TheMovieDBInterface
import com.example.movieapplication.data.vo.MovieDetails
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailsNetworkDataSource(private val apiService:TheMovieDBInterface) {

    private val _networkState=MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse=MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse:LiveData<MovieDetails>
        get()=_downloadedMovieDetailsResponse

    private val compositeDisposable = CompositeDisposable()

    fun fetchMovieDetails(movieId:Int){
        _networkState.postValue(NetworkState.LOADING)
        try{
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e( "MovieDetailsDataSource ",it.message!! )
                        }
                    )
            )
        }catch(e:Exception) {
            Log.e( "MovieDetailsDataSource ",e.message!!)
        }
    }
}