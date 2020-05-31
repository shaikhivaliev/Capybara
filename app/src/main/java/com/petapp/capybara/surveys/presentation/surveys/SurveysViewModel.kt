package com.petapp.capybara.surveys.presentation.surveys

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.common.domain.CommonRepository
import com.petapp.capybara.common.domain.dto.Mark
import com.petapp.capybara.surveys.domain.SurveysRepository
import com.petapp.capybara.surveys.domain.dto.Survey
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SurveysViewModel(
    private val repository: SurveysRepository,
    private val repositoryCommon: CommonRepository
) : BaseViewModel() {

    val marks = MutableLiveData<List<Mark>>()
    var surveys = MutableLiveData<List<Survey>>()
    var isShowMock = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    fun getMarks() {
        repositoryCommon.getMarks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    marks.value = it
                    Log.d("database", "get marks success")
                },
                {
                    Log.d("database", "get marks error")
                }
            ).connect()
    }


    fun getSurveys(typeId: String) {
        repository.getSurveys(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { if (it.isNotEmpty()) getMarks() }
            .subscribe(
                {
                    isShowMock.value = it.isEmpty()
                    surveys.value = it
                    Log.d("database", "get surveys success")
                },
                {
                    isShowMock.value = false
                    errorMessage.value = "Error"
                    Log.d("database", "get profiles error")
                }
            ).connect()
    }

  }