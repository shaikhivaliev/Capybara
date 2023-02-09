package com.petapp.capybara.core.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IStore<out State : Any, in Event : Any, out Effect : Any> {

    val state: StateFlow<State>

    val effects: Flow<Effect>

    fun dispatch(event: Event)

    fun launch(coroutineScope: CoroutineScope)
}
