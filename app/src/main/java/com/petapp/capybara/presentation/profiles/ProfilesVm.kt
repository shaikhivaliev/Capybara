package com.petapp.capybara.presentation.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
) : BaseViewModel() {

    private val _profilesState = MutableLiveData<DataState<List<Profile>>>()
    val profilesState: LiveData<DataState<List<Profile>>> get() = _profilesState

    init {
        getProfiles()
    }

    fun getProfiles() {
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        _profilesState.value = DataState.EMPTY
                    } else {
                        _profilesState.value = DataState.DATA(it)
                    }
                },
                {
                    _profilesState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun openProfileScreen(profile: Profile) {
        mainNavigator.openProfile(profile)
    }

    fun openNewProfile() {
        mainNavigator.openNewProfile()
    }
}
