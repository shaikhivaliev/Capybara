package com.petapp.capybara.data

import com.petapp.capybara.data.model.ItemHealthDiary
import com.petapp.capybara.database.AppDao
import io.reactivex.Single

class HealthDiaryRepositoryImpl(private val appDao: AppDao) : HealthDiaryRepository {

    override fun getItemsHealthDiary(): Single<List<ItemHealthDiary>> {
        return appDao.getItemHealthDiaryWithSurveys().map { it.toHealthDiaryItems() }
    }
}
