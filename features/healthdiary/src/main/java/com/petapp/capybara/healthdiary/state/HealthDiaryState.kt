package com.petapp.capybara.healthdiary.state

import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.SurveyHealthDiary
import com.petapp.capybara.core.mvi.SideEffect

data class HealthDiaryUI(
    val profiles: List<Profile>,
    val checkedProfileId: Long,
    val checkedHealthDiary: List<ItemHealthDiary>,
    val healthDiary: List<ItemHealthDiary>
)

sealed class HealthDiaryEffect : SideEffect {
    object Ready : HealthDiaryEffect()
    object ShowInfoDialog : HealthDiaryEffect()
    data class ShowDeleteDialog(val surveyId: Long) : HealthDiaryEffect()
    data class ShowAddingSurveyDialog(val title: Int) : HealthDiaryEffect()
}

