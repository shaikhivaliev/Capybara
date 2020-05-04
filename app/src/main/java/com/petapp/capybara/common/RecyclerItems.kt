package com.petapp.capybara.common

abstract class RecyclerItems {
    abstract val id: Int
    fun isSame(other: RecyclerItems) = id == other.id
}