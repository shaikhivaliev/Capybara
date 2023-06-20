package com.petapp.capybara.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.repository.ProfileRepository
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.mvi.SideEffect
import com.petapp.capybara.profile.state.ProfileEffect
import com.petapp.capybara.profile.state.ProfilesEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfilesVm(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profilesState = MutableStateFlow<DataState<List<Profile>>>(DataState.READY)
    val profilesState: StateFlow<DataState<List<Profile>>> get() = _profilesState.asStateFlow()

    private val _sideEffect = MutableStateFlow<SideEffect>(ProfilesEffect.Ready)
    val sideEffect: StateFlow<SideEffect> get() = _sideEffect.asStateFlow()

     fun getProfiles() {
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

    fun setSideEffect(effect: SideEffect) {
        _sideEffect.value = effect
    }
}
