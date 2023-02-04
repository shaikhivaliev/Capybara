package com.petapp.capybara.presentation.types

import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.mvi.IStore
import com.petapp.capybara.core.mvi.Store
import com.petapp.capybara.core.mvi.Next
import com.petapp.capybara.core.mvi.Update
import com.petapp.capybara.data.model.Type

class TypesStore(
    update: TypesUpdate
) : IStore<TypesState, TypesEvent, TypesEffect> by Store(TypesState(), update)

data class TypesState(
    val state: DataState<List<Type>> = DataState.LOADING
)

sealed class TypesEvent {
    object TypesEmpty : TypesEvent()
    data class TypesError(val error: Throwable) : TypesEvent()
    data class TypesLoaded(val types: List<Type>) : TypesEvent()
}

sealed class TypesEffect : TypesEvent()

class TypesUpdate : Update<TypesState, TypesEvent, TypesEffect> {
    override fun update(
        state: TypesState,
        event: TypesEvent,
    ): Next<TypesState, TypesEffect> {
        return when (event) {
            is TypesEvent.TypesEmpty -> {
                Next(
                    state = TypesState(DataState.EMPTY),
                )
            }
            is TypesEvent.TypesError -> {
                Next(
                    state = TypesState(DataState.ERROR()),
                )
            }
            is TypesEvent.TypesLoaded -> {
                Next(
                    state = TypesState(DataState.DATA(event.types)),
                )
            }
        }
    }
}
