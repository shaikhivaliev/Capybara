package com.petapp.capybara.presentation.healthDiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.HealthDiaryRepository
import com.petapp.capybara.data.model.ItemHealthDiary
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HealthDiaryViewModel(
    private val repository: HealthDiaryRepository
) : BaseViewModel() {

    private val _healthDiaryItems = MutableLiveData<List<ItemHealthDiary>>()
    val healthDiaryItems: LiveData<List<ItemHealthDiary>> = _healthDiaryItems

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    private val _expandedItem = MutableLiveData<ItemHealthDiary>()
    val expandedItem: LiveData<ItemHealthDiary> = _expandedItem

    init {
        getHealthDiaryItems()
    }

    private fun getHealthDiaryItems() {
        repository.getItemsHealthDiary()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _healthDiaryItems.value = it
                    Log.d(TAG, "get health diary items success")
                },
                {
                    _errorMessage.value = R.string.error_get_health_diary_items
                    Log.d(TAG, "get health diary items error")
                }
            ).connect()
    }

    fun handleStepClick(item: ItemHealthDiary) {
        _healthDiaryItems.value?.let { items ->
            items.find { it.id == item.id }?.also {
                it.isExpanded = item.isExpanded
                _expandedItem.value = it
            }
            _healthDiaryItems.value = items
        }
    }

//    private fun List<BaseActItem>.toSteps(): List<Step> = groupBy { it.type.stepType }.map { (stepType, actItems) ->
//        Step(
//            subSteps = actItems.map { SubStep(it.type, it.state, it.type.toScreen()) },
//            isExpanded = steps.value?.find { it.type == stepType }?.isExpanded ?: false,
//            isLocked = stepType == ActStepType.COMPLETION && hasIncompleteNonCompletionSteps()
//        )
//    }

    companion object {
        private const val TAG = "database_hd_items"
    }
}
