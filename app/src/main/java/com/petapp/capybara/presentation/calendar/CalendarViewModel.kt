package com.petapp.capybara.presentation.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.SurveysRepository
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CalendarViewModel(
    private val repositoryMark: MarksRepository,
    private val repositorySurveys: SurveysRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _marks = MutableLiveData<List<Mark>>()
    val marks: LiveData<List<Mark>> get() = _marks

    private val _surveys = MutableLiveData<List<Survey>>()
    val surveys: LiveData<List<Survey>> get() = _surveys

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    val profileId = MutableLiveData<String>()
    val month = MutableLiveData<String>()

    init {
        getMarks()
    }

    fun getSurveysByMonth() {
        if (month.value != null) {
            repositorySurveys.getSurveysByMonth(month.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _surveys.value = it.filter { item -> item.profileId == profileId.value }
                        Log.d(TAG, "get surveys success, ${it.size}")
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
