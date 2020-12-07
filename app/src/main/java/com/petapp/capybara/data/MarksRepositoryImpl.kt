package com.petapp.capybara.data

import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.database.AppDao
import io.reactivex.Single

class MarksRepositoryImpl(private val appDao: AppDao) : MarksRepository {

    override fun getMarks(): Single<List<Mark>> {
        return appDao.getProfiles().map { it.toMarks() }
    }
}
