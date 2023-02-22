package com.petapp.capybara.profile.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.mvi.SideEffect

sealed class ProfileMode {
    data class NEW(val data: ProfileNew) : ProfileMode()
    data class EDIT(val data: ProfileUI) : ProfileMode()
    data class READONLY(val data: ProfileUI) : ProfileMode()
}

data class ProfileNew(
    val color: Int? = null,
    val photoUri: String? = null
)

data class ProfileUI(
    val profile: Profile
)

data class ProfileInputData(
    val photoUri: MutableState<String> = mutableStateOf(""),
    val name: MutableState<String> = mutableStateOf(""),
    val color: MutableState<Int> = mutableStateOf(0)
)

sealed class ProfileEffect : SideEffect {
    object Ready : ProfileEffect()
    data class ShowDeleteDialog(val profileId: Long) : ProfileEffect()
    object ShowAddingColor : ProfileEffect()
    object NavigateToProfile : ProfileEffect()
    object ShowSnackbar : ProfileEffect()
    object ShowAddingPhoto : ProfileEffect()
}
