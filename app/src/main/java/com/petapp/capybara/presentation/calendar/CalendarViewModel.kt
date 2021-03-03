package com.petapp.capybara.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.R
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.SurveysRepository
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Months
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class CalendarViewModel(
    private val repositoryMark: MarksRepository,
    private val repositorySurveys: SurveysRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _marks = MutableLiveData<List<Mark>>()
    val marks: LiveData<List<Mark>> get() = _marks

    private val _surveys = MutableLiveData<Months>()
    val surveys: LiveData<Months> get() = _surveys

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    val profileId = MutableLiveData<Long>()
    val currentDate = MutableLiveData<Calendar>()

    init {
        getMarks()
    }

    fun getSurveysByMonth() {
        if (currentDate.value != null) {
            repositorySurveys.getSurveysByMonths(currentDate.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _surveys.value = Months(
                            it.previousMonthSurveys.filter { item -> item.profileId == profileId.value },
                            it.currentMonthSurveys.filter { item -> item.profileId == profileId.value },
                            it.nextMonthSurveys.filter { item -> item.profileId == profileId.value }
                        )
                        Log.d(TAG, "get surveys bu months success")
                    },
                    {
                        _errorMessage.value = R.string.error_get_marks
                        Log.d(TAG, "get surveys error")
                    }
                ).connect()
        }
    }

    private fun getMarks() {
        repositoryMark.getMarks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _marks.value = it
                    Log.d(TAG, "get marks success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get marks error")
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
