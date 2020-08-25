package com.petapp.capybara.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CalendarViewModel(
    private val repository: MarksRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _marks = MutableLiveData<List<Mark>>()
    val marks: LiveData<List<Mark>> get() = _marks

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    init {
        getMarks()
    }

    private fun getMarks() {
        repository.getMarks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _marks.value = it
                    Log.d(TAG, "Get marks success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "Get marks error")
                }
            ).connect()
    }

    fun openSurveyScreen(surveyId: String?, typeId: String?) {
        CalendarFragmentDirections.toSurvey(surveyId, typeId).navigateWith(navController)
    }

    companion object {
        private const val TAG = "database"
    }
}