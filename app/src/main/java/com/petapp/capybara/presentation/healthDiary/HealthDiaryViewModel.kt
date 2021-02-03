package com.petapp.capybara.presentation.healthDiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.HealthDiaryRepository
import com.petapp.capybara.data.MarksRepository
import com.petapp.capybara.data.model.ItemHealthDiary
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.SurveyHealthDiary
import com.petapp.capybara.extensions.navigateWith
import com.petapp.capybara.presentation.surveys.SurveysFragmentDirections
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HealthDiaryViewModel(
    private val repositoryHealthDiary: HealthDiaryRepository,
    private val repositoryMarks: MarksRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _healthDiaryItems = MutableLiveData<List<ItemHealthDiary>>()
    val healthDiaryItems: LiveData<List<ItemHealthDiary>> = _healthDiaryItems

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    private val _marks = MutableLiveData<List<Mark>>()
    val marks: LiveData<List<Mark>> get() = _marks

    val profileId = MutableLiveData<String>()

    init {
        getMarks()
    }

    fun getHealthDiaryItems() {
        repositoryHealthDiary.getItemsHealthDiary()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    val sortedItems = it.toRangeItems()
                    _healthDiaryItems.value = sortedItems.filtered(profileId.value)
                    Log.d(TAG, "get health diary items success")
                },
                {
                    _errorMessage.value = R.string.error_get_health_diary_items
                    Log.d(TAG, "get health diary items error")
                }
            ).connect()
    }

    fun createHealthDiarySurvey(survey: SurveyHealthDiary?) {
        if (survey != null) {
            repositoryHealthDiary.createSurveyHealthDiary(survey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        getHealthDiaryItems()
                        Log.d(TAG, "create health diary survey success")
                    },
                    {
                        _errorMessage.value = R.string.error_get_health_diary_items
                        Log.d(TAG, "create health diary survey error")
                    }
                ).connect()
        }
    }

    fun deleteHealthDiary(surveyId: String) {
        repositoryHealthDiary.deleteSurveyHealthDiary(surveyId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    getHealthDiaryItems()
                    Log.d(TAG, "delete health diary survey $surveyId success")
                },
                {
                    _errorMessage.value = R.string.error_delete_type
                    Log.d(TAG, "delete health diary survey error")
                }
            ).connect()
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

    fun openProfileScreen() {
        SurveysFragmentDirections.toProfiles().navigateWith(navController)
    }

    fun handleStepClick(item: ItemHealthDiary) {
        _healthDiaryItems.value?.let { items ->
            items.find { it.id == item.id }?.also {
                it.isExpanded = item.isExpanded
            }
            _healthDiaryItems.value = items
        }
    }

    private fun List<ItemHealthDiary>.toRangeItems(): List<ItemHealthDiary> =
        groupBy { it.type }.map { (itemType, items) ->
            ItemHealthDiary(
                id = itemType.ordinal,
                type = itemType,
                isExpanded = _healthDiaryItems.value?.find { it.type == itemType }?.isExpanded ?: false,
                surveys = items.find { itemType == it.type }?.surveys ?: emptyList()
            )
        }

    private fun List<ItemHealthDiary>.filtered(profileId: String?): List<ItemHealthDiary> {
        return this.map { item ->
            val surveys = item.surveys.filter { it.profileId == profileId }
            item.surveys = surveys
            item
        }
    }

    companion object {
        private const val TAG = "database_hd_items"
    }
}
