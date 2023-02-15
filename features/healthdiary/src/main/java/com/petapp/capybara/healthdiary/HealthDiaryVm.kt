package com.petapp.capybara.healthdiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.SurveyHealthDiary
import com.petapp.capybara.core.data.repository.HealthDiaryRepository
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.mvi.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HealthDiaryVm(
    private val healthDiaryRepository: HealthDiaryRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _healthDiaryState = MutableStateFlow<DataState<HealthDiaryUI>>(DataState.READY)
    val healthDiaryState: StateFlow<DataState<HealthDiaryUI>> = _healthDiaryState.asStateFlow()

    init {
        initProfiles()
    }

    private fun initProfiles() {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfiles()
            }
                .onSuccess {
                    if (it.isEmpty()) {
                        _healthDiaryState.value = DataState.EMPTY
                    } else {
                        initHealthDiaryItems(it)
                    }
                }
                .onFailure {
                    _healthDiaryState.value = DataState.ERROR(it)
                }
        }
    }

    private fun initHealthDiaryItems(profiles: List<Profile>) {
        viewModelScope.launch {
            runCatching {
                healthDiaryRepository.getItemsHealthDiary()
            }
                .onSuccess {
                    val firstProfileId = profiles.first().id
                    val healthDiaryUI = HealthDiaryUI(
                        profiles = profiles,
                        checkedProfileId = firstProfileId,
                        healthDiary = it,
                        checkedHealthDiary = it.filterHealthDiary(firstProfileId)
                    )
                    _healthDiaryState.value = DataState.DATA(healthDiaryUI)
                }
                .onFailure {
                    _healthDiaryState.value = DataState.ERROR(it)
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
                        resumeHealthDiaryItems()
                    }
                    .onFailure {
                        _healthDiaryState.value = DataState.ERROR(it)
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
                    resumeHealthDiaryItems()
                }
                .onFailure {
                    _healthDiaryState.value = DataState.ERROR(it)
                }
        }
    }

    private fun resumeHealthDiaryItems() {
        _healthDiaryState.value.onData { state ->
            viewModelScope.launch {
                runCatching {
                    healthDiaryRepository.getItemsHealthDiary()
                }
                    .onSuccess {
                        val expandedItems = it.expandItems(state.checkedHealthDiary)
                        val filteredItems = expandedItems.filterHealthDiary(state.checkedProfileId)
                        val healthDiaryUI = HealthDiaryUI(
                            profiles = state.profiles,
                            checkedProfileId = state.checkedProfileId,
                            healthDiary = it,
                            checkedHealthDiary = filteredItems
                        )
                        _healthDiaryState.value = DataState.DATA(healthDiaryUI)
                    }
                    .onFailure {
                        _healthDiaryState.value = DataState.ERROR(it)
                    }
            }
        }
    }

    fun expandItem(item: ItemHealthDiary) {
        _healthDiaryState.value.onData {
            val expandedItems = it.checkedHealthDiary.expandItem(item)
            val healthDiaryUI = HealthDiaryUI(
                profiles = it.profiles,
                checkedProfileId = it.checkedProfileId,
                healthDiary = it.healthDiary,
                checkedHealthDiary = expandedItems
            )
            _healthDiaryState.value = DataState.DATA(healthDiaryUI)
        }
    }

    fun getHealthDiaryItemsByProfile(profileId: Long) {
        _healthDiaryState.value.onData {
            val filteredItems = it.healthDiary.filterHealthDiary(profileId)
            val expandedItems = filteredItems.expandItems(it.checkedHealthDiary)
            val healthDiaryUI = HealthDiaryUI(
                profiles = it.profiles,
                checkedProfileId = profileId,
                healthDiary = it.healthDiary,
                checkedHealthDiary = expandedItems
            )
            _healthDiaryState.value = DataState.DATA(healthDiaryUI)
        }
    }

    fun openProfileScreen() {
        // mainNavigator.openProfiles()
    }
}

data class HealthDiaryUI(
    val profiles: List<Profile>,
    val checkedProfileId: Long,
    val checkedHealthDiary: List<ItemHealthDiary>,
    val healthDiary: List<ItemHealthDiary>
)
