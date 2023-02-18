package com.petapp.capybara.core.di

abstract class BaseComponentHolder<T : BaseComponent> {
    @Suppress("PropertyName")
    protected var _component: T? = null

    abstract val component: T

    fun clearComponent() {
        _component = null
    }
}
