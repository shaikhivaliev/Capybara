package com.petapp.capybara.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.R
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.data.ProfileRepository
import com.petapp.capybara.data.SurveysRepository
import com.petapp.capybara.data.model.*
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class CalendarViewModel(
    private val repositoryProfile: ProfileRepository,
    private val repositorySurveys: SurveysRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _initMonths = MutableLiveData<Months>()
    val initMonths: LiveData<Months> get() = _initMonths

    private val _nextMonth = MutableLiveData<Month>()
    val nextMonth: LiveData<Month> get() = _nextMonth

    private val _previousMonth = MutableLiveData<Month>()
    val previousMonth: LiveData<Month> get() = _previousMonth

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    val profileId = MutableLiveData<Long>()

    init {
        getProfiles()
    }

    fun getInitMonths(currentDate: Calendar) {
        repositorySurveys.getInitMonths(currentDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _initMonths.value = Months(
                        it.twoMonthAgoSurveys.filter { item -> item.profileId == profileId.value },
                        it.previousMonthSurveys.filter { item -> item.profileId == profileId.value },
                        it.currentMonthSurveys.filter { item -> item.profileId == profileId.value },
                        it.nextMonthSurveys.filter { item -> item.profileId == profileId.value },
                        it.nextTwoMonthSurveys.filter { item -> item.profileId == profileId.value }
                    )
                    Log.d(TAG, "get surveys bu months success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get surveys error")
                }
            ).connect()
    }

    fun getPreviousMonth(date: Calendar) {
        repositorySurveys.getSurveysByMonth(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _previousMonth.value = Month(surveys = it, calendar = date)
                    Log.d(TAG, "get surveys bu months success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get surveys error")
                }
            ).connect()
    }

    fun getNextMonth(date: Calendar) {
        repositorySurveys.getSurveysByMonth(date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _nextMonth.value = Month(surveys = it, calendar = date)
                    Log.d(TAG, "get surveys bu months success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get surveys error")
                }
            ).connect()
    }

    private fun getProfiles() {
        repositoryProfile.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _profiles.value = it
                    Log.d(TAG, "get profiles success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    fun openSurveyScreen(survey: Survey?) {
        CalendarFragmentDirections.toSurvey(survey).navigateWith(navController)
    }

    fun openProfileScreen() {
        CalendarFragmentDirections.toProfiles().navigateWith(navController)
    }

    companion object {
        private const val TAG = "database_calendar"
    }
}
