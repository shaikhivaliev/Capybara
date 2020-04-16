package com.petapp.capybara.presenter

import com.petapp.capybara.model.Repository
import com.petapp.capybara.view.MainView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PresenterImpl(
    private val view: MainView,
    private val repository: Repository
) : Presenter {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onDateClicked(id: Int) {
        viewModelScope.launch {
            repository.getData(
                data = { view.showData(it.departmentCaption) },
                error = { view.showError(it) },
                inProgress = { view.inProgress(it) }
            )
        }
    }

    override fun onDestroy() {
        viewModelJob.cancel()
    }
}