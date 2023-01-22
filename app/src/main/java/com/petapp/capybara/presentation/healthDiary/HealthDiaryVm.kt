package com.petapp.capybara.presentation.healthDiary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.state.DataState
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IHealthDiaryRepository
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.presentation.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                        healthDiaryList = it
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
                        val checkedExpandedItems = it.expandItems(state.healthDiaryList)
                        val healthDiaryUI = HealthDiaryUI(
                            profiles = state.profiles,
                            checkedProfileId = state.checkedProfileId,
                            healthDiaryList = checkedExpandedItems
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
            val filteredItems = it.healthDiaryList.expandItem(item)
            val healthDiaryUI = HealthDiaryUI(
                profiles = it.profiles,
                checkedProfileId = it.checkedProfileId,
                healthDiaryList = filteredItems
            )
            _healthDiaryState.value = DataState.DATA(healthDiaryUI)
        }
    }

    fun getHealthDiaryItemsByProfile(profileId: Long) {
        _healthDiaryState.value.onData {
            val filteredItems = it.healthDiaryList.filterHealthDiary(profileId)
            val healthDiaryUI = HealthDiaryUI(
                profiles = it.profiles,
                checkedProfileId = profileId,
                healthDiaryList = filteredItems
            )
            _healthDiaryState.value = DataState.DATA(healthDiaryUI)
        }
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }
}

data class HealthDiaryUI(
    val profiles: List<Profile>,
    val checkedProfileId: Long,
    val healthDiaryList: List<ItemHealthDiary>
)

