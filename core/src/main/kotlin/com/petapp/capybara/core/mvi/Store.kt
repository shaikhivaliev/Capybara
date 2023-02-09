package com.petapp.capybara.core.mvi

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class Store<State : Any, Event : Any, UiEvent : Event, Effect : Any>(
    initialState: State,
    private val update: Update<State, Event, Effect>
) : IStore<State, UiEvent, Effect> {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state

    private val _effects: MutableSharedFlow<Effect> = MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
    override val effects: SharedFlow<Effect> = _effects

    private val events: MutableSharedFlow<Event> = MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)

    override fun dispatch(event: UiEvent) {
        if (!events.tryEmit(event)) error("Couldn't process $event, flow buffer overflow")
    }

    override fun launch(coroutineScope: CoroutineScope) {
        coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
            events.collect { event ->
                val next = update.update(state.value, event)
                _state.value = next.state

                for (effect in next.effects) {
                    if (!_effects.tryEmit(effect)) error("Couldn't process $effect, flow buffer overflow")
                }
            }
        }
    }
}

