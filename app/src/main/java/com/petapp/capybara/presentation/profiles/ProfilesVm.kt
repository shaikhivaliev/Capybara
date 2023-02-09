package com.petapp.capybara.presentation.profiles

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.navigation.MainNavigatorImpl
import com.petapp.capybara.viewmodel.SavedStateVmAssistedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfilesVmFactory(
    private val mainNavigator: MainNavigatorImpl,
    private val profileRepository: ProfileRepository
) : SavedStateVmAssistedFactory<ProfilesVm> {
    override fun create(handle: SavedStateHandle) =
        ProfilesVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            profileRepository = profileRepository
        )
}

class ProfilesVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: MainNavigatorImpl,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profilesState = MutableStateFlow<DataState<List<Profile>>>(DataState.READY)
    val profilesState: StateFlow<DataState<List<Profile>>> get() = _profilesState.asStateFlow()

    init {
        getProfiles()
    }

    private fun getProfiles() {
        viewModelScope.launch {
            runCatching {
                profileRepository.getProfiles()
            }
                .onSuccess {
                    if (it.isEmpty()) {
                        _profilesState.value = DataState.EMPTY
                    } else {
                        _profilesState.value = DataState.DATA(it)
                    }
                }
                .onFailure {
                    _profilesState.value = DataState.ERROR(it)
                }
        }
    }

    fun openProfileScreen(profile: Profile) {
        mainNavigator.openProfile(profile)
    }

    fun openNewProfile() {
        mainNavigator.openNewProfile()
    }
}
