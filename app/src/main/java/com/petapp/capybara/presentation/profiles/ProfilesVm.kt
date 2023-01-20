package com.petapp.capybara.presentation.profiles

import androidx.lifecycle.*
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import kotlinx.coroutines.launch

class ProfilesVmFactory(
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository
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
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository
) : ViewModel() {

    private val _profilesState = MutableLiveData<DataState<List<Profile>>>()
    val profilesState: LiveData<DataState<List<Profile>>> get() = _profilesState

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
