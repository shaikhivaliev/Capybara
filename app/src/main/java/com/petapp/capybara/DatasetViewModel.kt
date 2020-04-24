package com.petapp.capybara

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DatasetViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val inProgress = MutableLiveData<Boolean>()
    val data = MutableLiveData<Dataset>()
    val error = MutableLiveData<String>()

    fun getData(id: Int) {
        viewModelScope.launch {
            val result = apiResult(
                apiCall = { Network.api.getDataset(id) },
                inProgress = { inProgress.value = it }
            )
            when (result) {
                is Result.Success -> data.value = result.data
                is Result.Error -> error.value = result.exception.errorMessage()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}