package com.petapp.capybara.ui.common

abstract class RecyclerItems {
    abstract val id: Int
    fun isSame(other: RecyclerItems) = id == other.id
}