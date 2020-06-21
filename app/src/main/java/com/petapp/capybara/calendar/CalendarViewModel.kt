package com.petapp.capybara.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.domain.CommonRepository
import com.petapp.capybara.common.domain.dto.Mark
import com.petapp.capybara.common.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CalendarViewModel(
    private val repository: CommonRepository
) : BaseViewModel() {

    val marks = MutableLiveData<List<Mark>>()

    fun getMarks() {
        repository.getMarks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    marks.value = it
                    Log.d("database", "Get marks success")
                },
                {
                    Log.d("database", "Get marks error")
                }
            ).connect()
    }
}