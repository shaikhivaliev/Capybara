package com.petapp.capybara.data

import com.petapp.capybara.data.model.ItemHealthDiary
import com.petapp.capybara.data.model.SurveyHealthDiary
import io.reactivex.Completable
import io.reactivex.Single

interface HealthDiaryRepository {

    fun getItemsHealthDiary(): Single<List<ItemHealthDiary>>

    fun createSurveyHealthDiary(survey: SurveyHealthDiary): Completable

    fun deleteSurveyHealthDiary(surveyId: String): Completable
}
