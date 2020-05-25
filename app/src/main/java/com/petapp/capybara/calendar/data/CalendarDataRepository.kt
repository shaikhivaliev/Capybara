package com.petapp.capybara.calendar.data

import com.petapp.capybara.calendar.domain.CalendarRepository
import com.petapp.capybara.calendar.domain.Mark
import com.petapp.capybara.database.AppDao
import io.reactivex.Single
import javax.inject.Inject

class CalendarDataRepository @Inject constructor(
    private val appDao: AppDao,
    private val mapper: CalendarEntityMapper
) : CalendarRepository {

    override fun getMarks(): Single<List<Mark>> {
        return appDao.getProfiles().map(mapper::transformToMark)
    }
}