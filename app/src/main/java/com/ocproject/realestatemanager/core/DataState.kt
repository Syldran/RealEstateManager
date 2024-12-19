package com.ocproject.realestatemanager.core

sealed class DataState<T> {

    data class Success<T>(val data: T) : DataState<T>()

    data class Error<T>(val exception: Exception) : DataState<T>()

    data class Loading<T>(val isLoading: Boolean): DataState<T>()
}