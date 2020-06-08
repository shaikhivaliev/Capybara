package com.petapp.capybara.common.data

import com.petapp.capybara.common.domain.CommonRepository
import com.petapp.capybara.common.domain.dto.Mark
import com.petapp.capybara.database.AppDao
import io.reactivex.Single

class CommonDataRepository(
    private val appDao: AppDao,
    private val mapper: CommonEntityMapper
) :
    CommonRepository {

    override fun getMarks(): Single<List<Mark>> {
        return appDao.getProfiles().map(mapper::transformToMark)
    }
}