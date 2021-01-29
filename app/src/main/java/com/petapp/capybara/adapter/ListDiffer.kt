package com.petapp.capybara.adapter

interface ListDiffer<T> {

    fun areItemsTheSame(other: T): Boolean

    fun areContentsTheSame(other: T): Boolean
}
