package com.example.movieapplication.data.repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}

class NetworkState(val status:Status, val msg:String) {
    companion object{
        val LOADED:NetworkState = NetworkState(Status.SUCCESS,"Успешно")
        val LOADING:NetworkState = NetworkState(Status.RUNNING, "Загрузка")
        val ERROR:NetworkState = NetworkState(Status.FAILED, "Что-то пошло не так")
        val ENDOFLIST:NetworkState= NetworkState(Status.FAILED,"Вы достигли конца списка")
    }
}