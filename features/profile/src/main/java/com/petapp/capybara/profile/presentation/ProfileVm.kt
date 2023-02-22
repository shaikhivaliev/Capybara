package com.petapp.capybara.profile

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.mvi.SideEffect
import com.petapp.capybara.profile.state.*
import com.petapp.capybara.styles.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileVm(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<DataState<ProfileMode>>(DataState.READY)
    val profileState: StateFlow<DataState<ProfileMode>> get() = _profileState.asStateFlow()

    private val _sideEffect = MutableStateFlow<SideEffect>(ProfileEffect.Ready)
    val sideEffect: StateFlow<SideEffect> get() = _sideEffect.asStateFlow()

    fun getProfile(profileId: Long?) {
        if (profileId == null) {
            _profileState.value = DataState.DATA(
                ProfileMode.NEW(ProfileNew())
            )
        } else {
            getProfile(profileId)
        }
    }

    private fun getProfile(profileId: Long) {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfile(profileId)
            }
                .onSuccess {
                    val profile = ProfileUI(it)
                    _profileState.value = DataState.DATA(
                        ProfileMode.READONLY(profile)
                    )
                }
                .onFailure {
                    _profileState.value = DataState.ERROR(it)
                }
        }
    }

    fun toEditMode(data: ProfileUI) {
        _profileState.value = DataState.DATA(
            ProfileMode.EDIT(data)
        )
    }

    fun verifyProfile(
        mode: ProfileMode,
        profileInputData: ProfileInputData
    ) {
        if (
            profileInputData.name.value.isEmpty() ||
            profileInputData.color.value == 0
        ) {
            _sideEffect.value = ProfileEffect.ShowSnackbar
        } else {
            val profile = createProfile(mode, profileInputData) ?: return

            viewModelScope.launch {
                runCatching {
                    if (mode is ProfileMode.NEW) {
                        profileRepository.createProfile(profile)
                    } else {
                        profileRepository.updateProfile(profile)
                    }
                }
                    .onSuccess {
                        setSideEffect(ProfileEffect.NavigateToProfile)
                    }
                    .onFailure {
                        _profileState.value = DataState.ERROR(it)
                    }
            }
        }
    }

    private fun createProfile(mode: ProfileMode, profileInputData: ProfileInputData): Profile? {
        return when (mode) {
            is ProfileMode.EDIT -> {
                Profile(
                    id = mode.data.profile.id,
                    name = profileInputData.name.value,
                    color = profileInputData.color.value,
                    photo = profileInputData.photoUri.value,
                    surveys = mode.data.profile.surveys
                )
            }
            is ProfileMode.NEW -> {
                Profile(
                    id = 0L,
                    name = profileInputData.name.value,
                    color = profileInputData.color.value,
                    photo = profileInputData.photoUri.value
                )
            }
            else -> null
        }
    }

    fun deleteProfile(profileId: Long?) {
        if (profileId != null) {
            viewModelScope.launch {
                runCatching {
                    profileRepository.deleteProfile(profileId)
                }
                    .onSuccess {
                        setSideEffect(ProfileEffect.NavigateToProfile)
                    }
                    .onFailure {
                        _profileState.value = DataState.ERROR(it)
                    }
            }
        }
    }

    fun updatePhoto(uri: String) {
        _profileState.value.onData { mode ->
            _profileState.value = when (mode) {
                is ProfileMode.EDIT -> {
                    val profile = mode.data.profile.copy(photo = uri)
                    val data = mode.data.copy(profile = profile)
                    DataState.DATA(mode.copy(data = data))
                }
                is ProfileMode.NEW -> {
                    val data = mode.data.copy(photoUri = uri)
                    DataState.DATA(mode.copy(data = data))
                }
                is ProfileMode.READONLY -> {
                    DataState.DATA(mode)
                }
            }
        }
    }

    fun updateColor(color: Int) {
        _profileState.value.onData { mode ->
            _profileState.value = when (mode) {
                is ProfileMode.EDIT -> {
                    val profile = mode.data.profile.copy(color = color)
                    val data = mode.data.copy(profile = profile)
                    DataState.DATA(mode.copy(data = data))
                }
                is ProfileMode.NEW -> {
                    val data = mode.data.copy(color = color)
                    DataState.DATA(mode.copy(data = data))
                }
                is ProfileMode.READONLY -> {
                    DataState.DATA(mode)
                }
            }
        }
    }

    fun setSideEffect(effect: SideEffect) {
        _sideEffect.value = effect
    }

    companion object {
        val COLORS: List<Pair<Color, Int>> = listOf(
            Pair(red500, R.color.red_500),
            Pair(deepPurple500, R.color.deep_purple_500),
            Pair(indigo500, R.color.indigo_500),
            Pair(lightBlue500, R.color.light_blue_500),
            Pair(cyan500, R.color.cyan_500),
            Pair(teal500, R.color.teal_500),
            Pair(green500, R.color.green_500),
            Pair(lime500, R.color.lime_500),
            Pair(yellow500, R.color.yellow_500),
            Pair(amber500, R.color.amber_500),
            Pair(deepOrange500, R.color.deep_orange_500)
        )
    }
}
