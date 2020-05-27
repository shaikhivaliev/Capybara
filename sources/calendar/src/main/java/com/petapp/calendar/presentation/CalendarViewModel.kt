package com.petapp.calendar.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.calendar.BaseViewModel
import com.petapp.capybara.calendar.domain.CalendarRepository
import com.petapp.capybara.calendar.domain.Mark
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository
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