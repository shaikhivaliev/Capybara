package com.petapp.capybara.calendar.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.calendar.domain.CalendarRepository
import com.petapp.capybara.calendar.domain.Mark
import com.petapp.capybara.common.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository,
    private val router: Router
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