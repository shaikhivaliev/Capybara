package com.petapp.capybara.presentation.healthDiary

import androidx.lifecycle.*
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IHealthDiaryRepository
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import kotlinx.coroutines.launch

class HealthDiaryVmFactory(
    private val mainNavigator: IMainNavigator,
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
    private val mainNavigator: IMainNavigator,
    private val healthDiaryRepository: IHealthDiaryRepository,
    private val profileRepository: IProfileRepository
) : ViewModel() {

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
        viewModelScope.launch {
            runCatching {
                healthDiaryRepository.getItemsHealthDiary()
            }
                .onSuccess {
                    val sortedItems = it.toRangeItems()
                    _healthDiaryItems.value = sortedItems.filtered(profileId.value)
                }
                .onFailure {
                    _errorMessage.value = R.string.error_get_health_diary_items
                }
        }
    }

    fun createHealthDiarySurvey(survey: SurveyHealthDiary?) {
        if (survey != null) {
            viewModelScope.launch {
                runCatching {
                    healthDiaryRepository.createSurveyHealthDiary(survey)
                }
                    .onSuccess {
                        getHealthDiaryItems()
                    }
                    .onFailure {
                        _errorMessage.value = R.string.error_get_health_diary_items
                    }
            }
        }
    }

    fun deleteHealthDiary(surveyId: Long) {
        viewModelScope.launch {
            runCatching {
                healthDiaryRepository.deleteSurveyHealthDiary(surveyId)
            }
                .onSuccess {
                    getHealthDiaryItems()
                }
                .onFailure {
                    _errorMessage.value = R.string.error_delete_type
                }
        }
    }

    private fun getMarks() {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfiles()
            }
                .onSuccess {
                    _profiles.value = it
                }
                .onFailure {
                    _errorMessage.value = R.string.error_get_marks

                }
        }
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
}
