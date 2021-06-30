package com.petapp.capybara.data

import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import io.reactivex.Completable
import io.reactivex.Single

interface IHealthDiaryRepository {

    fun getItemsHealthDiary(): Single<List<ItemHealthDiary>>

    fun createSurveyHealthDiary(survey: SurveyHealthDiary): Completable

    fun deleteSurveyHealthDiary(surveyId: Long): Completable
}
