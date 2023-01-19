package com.petapp.capybara.data

import com.petapp.capybara.data.model.Months
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.database.AppDao
import com.petapp.capybara.extensions.currentDateMonthYear
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function5
import io.reactivex.schedulers.Schedulers
import java.util.*

class SurveysRepository(private val appDao: AppDao) : ISurveysRepository {

    override fun getSurveysByType(typeId: Long): Single<List<Survey>> {
        return appDao.getSurveysByType(typeId).map { it.toSurveys() }
    }

    override fun getInitMonths(currentDate: Calendar): Single<Months> {
        val twoMonthAgo = (currentDate.clone() as Calendar).let {
            it.add(Calendar.MONTH, -TWO_MONTH)
            currentDateMonthYear(it.time)
        }
        val previousMonth = (currentDate.clone() as Calendar).let {
            it.add(Calendar.MONTH, -ONE_MONTH)
            currentDateMonthYear(it.time)
        }
        val currentMonth = currentDateMonthYear(currentDate.time)
        val nextMonth = (currentDate.clone() as Calendar).let {
            it.add(Calendar.MONTH, ONE_MONTH)
            currentDateMonthYear(it.time)
        }
        val nextTwoMonth = (currentDate.clone() as Calendar).let {
            it.add(Calendar.MONTH, TWO_MONTH)
            currentDateMonthYear(it.time)
        }

        return Single.zip(
            appDao.getSurveysByMonth(twoMonthAgo).map { it.toSurveys() },
            appDao.getSurveysByMonth(previousMonth).map { it.toSurveys() },
            appDao.getSurveysByMonth(currentMonth).map { it.toSurveys() },
            appDao.getSurveysByMonth(nextMonth).map { it.toSurveys() },
            appDao.getSurveysByMonth(nextTwoMonth).map { it.toSurveys() },
            Function5 { twoMonthAgo, previousMonth, currentMonth, nextMonth, nextTwoMonth ->
                Months(
                    twoMonthAgoSurveys = twoMonthAgo,
                    previousMonthSurveys = previousMonth,
                    currentMonthSurveys = currentMonth,
                    nextMonthSurveys = nextMonth,
                    nextTwoMonthSurveys = nextTwoMonth
                )
            }
        )
    }

    override fun getSurveysByMonth(currentDate: Calendar): Single<List<Survey>> {
        return appDao.getSurveysByMonth(currentDateMonthYear(currentDate.time)).map { it.toSurveys() }
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

    companion object {
        const val ONE_MONTH = 1
        const val TWO_MONTH = 2
    }
}
