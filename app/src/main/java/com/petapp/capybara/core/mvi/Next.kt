package com.petapp.capybara.core.mvi

class Next<out State: Any, out Effect: Any>(
    val state: State,
    val effects: List<Effect> = emptyList()
)
