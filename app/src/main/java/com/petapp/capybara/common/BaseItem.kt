package com.petapp.capybara.common

abstract class BaseItem {

    abstract val id: Int

    fun isSame(other: BaseItem) = id == other.id

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
