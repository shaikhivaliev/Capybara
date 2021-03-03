package com.petapp.capybara.data

import com.petapp.capybara.data.model.Months
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.database.AppDao
import com.petapp.capybara.extensions.currentDateMonthYear
import com.petapp.capybara.presentation.calendar.CalendarFragment.Companion.ONE_MONTH
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.*

class SurveysRepositoryImpl(private val appDao: AppDao) : SurveysRepository {

    override fun getSurveysByType(typeId: Long): Single<List<Survey>> {
        return appDao.getSurveysByType(typeId).map { it.toSurveys() }
    }

    override fun getSurveysByMonths(currentDate: Calendar): Single<Months> {

        val previousMonth = (currentDate.clone() as Calendar).let {
            it.add(Calendar.MONTH, -ONE_MONTH)
            currentDateMonthYear(it.time)
        }
        val currentMonth = currentDateMonthYear(currentDate.time)
        val nextMonth = (currentDate.clone() as Calendar).let {
            it.add(Calendar.MONTH, ONE_MONTH)
            currentDateMonthYear(it.time)
        }

        return Single.zip(
            appDao.getSurveysByMonth(previousMonth).map { it.toSurveys() },
            appDao.getSurveysByMonth(currentMonth).map { it.toSurveys() },
            appDao.getSurveysByMonth(nextMonth).map { it.toSurveys() },
            Function3 { previousMonthSurveys, currentMonthSurveys, nextMonthSurveys ->
                Months(
                    previousMonthSurveys = previousMonthSurveys,
                    currentMonthSurveys = currentMonthSurveys,
                    nextMonthSurveys = nextMonthSurveys
                )
            }
        )
    }

    override fun getSurvey(surveyId: Long): Single<Survey> {
        return appDao.getSurvey(surveyId).map { it.toSurvey() }
    }

    override fun createSurvey(survey: Survey): Completable {
        return Completable.fromAction { appDao.createSurvey(survey.toSurveyEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateSurvey(survey: Survey): Completable {
        return Completable.fromAction { appDao.updateSurvey(survey.toSurveyEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteSurvey(surveyId: Long): Completable {
        return Completable.fromAction { appDao.deleteSurvey(surveyId) }
            .subscribeOn(Schedulers.io())
    }
}
