package com.petapp.capybara.core.mvi

interface Update<State: Any, in Event: Any, out Effect: Any> {

    fun update(state: State, event: Event): Next<State, Effect>
}
