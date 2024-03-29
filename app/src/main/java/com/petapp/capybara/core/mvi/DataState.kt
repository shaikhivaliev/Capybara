package com.petapp.capybara.core.mvi

sealed class DataState<out TData> {

    object READY : DataState<Nothing>()

    object LOADING : DataState<Nothing>()

    object EMPTY : DataState<Nothing>()

    data class DATA<out TData>(val data: TData) : DataState<TData>()

    data class ERROR(val error: Throwable? = null) : DataState<Nothing>()

    inline fun onData(block: (TData) -> Unit) {
        if (this is DATA) {
            block(this.data)
        }
    }
}

sealed class SideEffect {
    object READY : SideEffect()
    object ACTION : SideEffect()
}

