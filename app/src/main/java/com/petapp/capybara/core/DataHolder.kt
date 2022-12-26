package com.petapp.capybara.core

sealed class DataState<out TData> {

    object LOADING : DataState<Nothing>()

    object EMPTY : DataState<Nothing>()

    object ACTION : DataState<Nothing>()

    data class DATA<out TData>(val data: TData) : DataState<TData>()

    data class ERROR(val error: Any?) : DataState<Nothing>()

}

