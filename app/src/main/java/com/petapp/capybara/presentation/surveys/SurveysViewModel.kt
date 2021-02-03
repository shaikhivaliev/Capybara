package com.petapp.capybara.presentation.surveys

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

class SurveysViewModel(
    private val repositorySurveys: SurveysRepository,
    private val repositoryMarks: MarksRepository,
    private val navController: NavController,
    private val typeId: String
) : BaseViewModel() {

    private val _marks = MutableLiveData<List<Mark>>()
    val marks: LiveData<List<Mark>> get() = _marks

    private val _surveys = MutableLiveData<List<Survey>>()
    val surveys: LiveData<List<Survey>> get() = _surveys

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    val profileId = MutableLiveData<String>()

    init {
        getMarks()
    }

    private fun getMarks() {
        repositoryMarks.getMarks()
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

    fun getSurveys() {
        repositorySurveys.getSurveysByType(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _surveys.value = it.filter { item -> item.profileId == profileId.value }
                    Log.d(TAG, "get surveys success")
                },
                {
                    _errorMessage.value = R.string.error_get_surveys
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    fun openSurveyScreen(survey: Survey?) {
        SurveysFragmentDirections.toSurvey(survey).navigateWith(navController)
    }

    fun openProfileScreen() {
        SurveysFragmentDirections.toProfiles().navigateWith(navController)
    }

    companion object {
        private const val TAG = "database_surveys"
    }
}
