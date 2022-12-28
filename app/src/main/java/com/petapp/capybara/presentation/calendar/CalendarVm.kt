package com.petapp.capybara.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.ISurveysRepository
import com.petapp.capybara.data.model.Month
import com.petapp.capybara.data.model.Months
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class CalendarVmFactory(
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository,
    private val surveysRepository: ISurveysRepository
) : SavedStateVmAssistedFactory<CalendarVm> {
    override fun create(handle: SavedStateHandle) =
        CalendarVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            profileRepository = profileRepository,
            surveysRepository = surveysRepository
        )
}

class CalendarVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository,
    private val surveysRepository: ISurveysRepository
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
        surveysRepository.getInitMonths(currentDate)
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
        surveysRepository.getSurveysByMonth(date)
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
        surveysRepository.getSurveysByMonth(date)
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
        profileRepository.getProfiles()
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
        if (survey != null) mainNavigator.openSurvey(survey)
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }

    companion object {
        private const val TAG = "database_calendar"
    }
}
