package com.petapp.capybara.presentation.healthDiary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainCoordinator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IHealthDiaryRepository
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HealthDiaryVmFactory(
    private val mainNavigator: IMainCoordinator,
    private val healthDiaryRepository: IHealthDiaryRepository,
    private val profileRepository: IProfileRepository
) : SavedStateVmAssistedFactory<HealthDiaryVm> {
    override fun create(handle: SavedStateHandle) =
        HealthDiaryVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            healthDiaryRepository = healthDiaryRepository,
            profileRepository = profileRepository
        )
}

class HealthDiaryVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainCoordinator,
    private val healthDiaryRepository: IHealthDiaryRepository,
    private val profileRepository: IProfileRepository
) : BaseViewModel() {

    private val _healthDiaryItems = MutableLiveData<List<ItemHealthDiary>>()
    val healthDiaryItems: LiveData<List<ItemHealthDiary>> = _healthDiaryItems

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    val profileId = MutableLiveData<Long>()

    init {
        getMarks()
    }

    fun getHealthDiaryItems() {
        healthDiaryRepository.getItemsHealthDiary()
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
            healthDiaryRepository.createSurveyHealthDiary(survey)
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

    fun deleteHealthDiary(surveyId: Long) {
        healthDiaryRepository.deleteSurveyHealthDiary(surveyId)
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
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _profiles.value = it
                    Log.d(TAG, "get profiles success")
                },
                {
                    _errorMessage.value = R.string.error_get_marks
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
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

            val isExpanded = _healthDiaryItems.value?.find { it.type == itemType }?.isExpanded ?: false
            val surveys = items.find { itemType == it.type }?.surveys ?: emptyList()

            ItemHealthDiary(
                id = itemType.ordinal.toLong(),
                type = itemType,
                isExpanded = isExpanded,
                surveys = surveys
            )
        }

    private fun List<ItemHealthDiary>.filtered(profileId: Long?): List<ItemHealthDiary> {
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
