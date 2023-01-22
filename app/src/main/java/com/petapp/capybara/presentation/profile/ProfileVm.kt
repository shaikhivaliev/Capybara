package com.petapp.capybara.presentation.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.state.DataState
import com.petapp.capybara.core.state.SideEffect
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileVmFactory(
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository
) : SavedStateVmAssistedFactory<ProfileVm> {
    override fun create(handle: SavedStateHandle) =
        ProfileVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            profileRepository = profileRepository
        )
}

class ProfileVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<DataState<ProfileMode>>(DataState.READY)
    val profileState: StateFlow<DataState<ProfileMode>> get() = _profileState.asStateFlow()

    private val _sideEffect = MutableStateFlow<SideEffect>(SideEffect.READY)
    val sideEffect: StateFlow<SideEffect> get() = _sideEffect.asStateFlow()

    fun getProfile(profileId: Long?) {
        if (profileId == null) {
            _profileState.value = DataState.DATA(
                ProfileMode.NEW(
                    ProfileNew(
                        colors = COLORS
                    )
                )
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
                    val profile = ProfileUI(
                        colors = COLORS,
                        profile = it
                    )
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
            _sideEffect.value = SideEffect.ACTION
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
                        openProfilesScreen()
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

    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            runCatching {
                profileRepository.deleteProfile(profileId)
            }
                .onSuccess {
                    openProfilesScreen()
                }
                .onFailure {
                    _profileState.value = DataState.ERROR(it)
                }
        }
    }

    private fun openProfilesScreen() {
        mainNavigator.openProfiles()
    }

    fun dismissSnackbar() {
        _sideEffect.value = SideEffect.READY
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

    companion object {
        private val COLORS: List<Int> = listOf(
            R.color.red_500,
            R.color.pink_500,
            R.color.deep_purple_500,
            R.color.indigo_500,
            R.color.light_blue_500,
            R.color.cyan_500,
            R.color.teal_500,
            R.color.green_500,
            R.color.lime_500,
            R.color.yellow_500,
            R.color.amber_500,
            R.color.deep_orange_500
        )
    }
}

sealed class ProfileMode {
    data class NEW(val data: ProfileNew) : ProfileMode()
    data class EDIT(val data: ProfileUI) : ProfileMode()
    data class READONLY(val data: ProfileUI) : ProfileMode()
}

data class ProfileNew(
    val photoUri: String? = null,
    val colors: List<Int>
)

data class ProfileUI(
    val colors: List<Int>,
    val profile: Profile
)

data class ProfileInputData(
    val photoUri: MutableState<String> = mutableStateOf(""),
    val name: MutableState<String> = mutableStateOf(""),
    val color: MutableState<Int> = mutableStateOf(0)
)
