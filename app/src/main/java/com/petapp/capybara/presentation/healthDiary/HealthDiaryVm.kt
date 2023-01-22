package com.petapp.capybara.presentation.healthDiary

import android.util.Log
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
import com.petapp.capybara.presentation.toInitItemHealthItems
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
                    val sortedItems = it.toInitItemHealthItems()
                    val firstProfileId = profiles.first().id
                    val healthDiaryUI = HealthDiaryUI(
                        profiles = profiles,
                        checkedProfileId = firstProfileId,
                        healthDiaryList = sortedItems
                    )
                    _healthDiaryState.value = DataState.DATA(healthDiaryUI)
                }
                .onFailure {
                    _healthDiaryState.value = DataState.ERROR(it)
                }
        }
    }

    fun getHealthDiaryItemsByProfile(profileId: Long) {
    }

    fun createHealthDiarySurvey(survey: SurveyHealthDiary?) {
        if (survey != null) {
            viewModelScope.launch {
                runCatching {
                    healthDiaryRepository.createSurveyHealthDiary(survey)
                }
                    .onSuccess {
                       // initHealthDiaryItems()
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
                   // initHealthDiaryItems()
                }
                .onFailure {
                    _healthDiaryState.value = DataState.ERROR(it)
                }
        }
    }

    fun openProfileScreen() {
        mainNavigator.openProfiles()
    }

    fun expandItem(it: ItemHealthDiary) {
        // todo
    }
//    fun handleStepClick(item: ItemHealthDiary) {
//        _healthDiaryItems.value?.let { items ->
//            items.find { it.id == item.id }?.also {
//                it.isExpanded = item.isExpanded
//            }
//            _healthDiaryItems.value = items
//        }
//    }
//
//    private fun List<ItemHealthDiary>.toRangeItems(): List<ItemHealthDiary> =
//        groupBy { it.type }.map { (itemType, items) ->
//
//            val isExpanded = _healthDiaryItems.value?.find { it.type == itemType }?.isExpanded ?: false
//            val surveys = items.find { itemType == it.type }?.surveys ?: emptyList()
//
//            ItemHealthDiary(
//                id = itemType.ordinal.toLong(),
//                type = itemType,
//                isExpanded = isExpanded,
//                surveys = surveys
//            )
//        }
//
//    private fun List<ItemHealthDiary>.filtered(profileId: Long?): List<ItemHealthDiary> {
//        return this.map { item ->
//            val surveys = item.surveys.filter { it.profileId == profileId }
//            item.surveys = surveys
//            item
//        }
//    }
}

data class HealthDiaryUI(
    val profiles: List<Profile>,
    val checkedProfileId: Long,
    val healthDiaryList: List<ItemHealthDiary>
)

