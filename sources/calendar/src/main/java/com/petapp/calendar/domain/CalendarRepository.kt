package com.petapp.capybara.calendar.domain

import io.reactivex.Single

interface CalendarRepository {

    fun getMarks(): Single<List<Mark>>

}