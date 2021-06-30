package com.petapp.capybara.presentation.profiles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
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

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _isShowMock = MutableLiveData<Boolean>()
    val isShowMock: LiveData<Boolean> get() = _isShowMock

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    init {
        getProfiles()
    }

    fun getProfiles() {
        profileRepository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isShowMock.value = it.isEmpty()
                    _profiles.value = it
                    Log.d(TAG, "get profiles success")
                },
                {
                    _errorMessage.value = R.string.error_get_profiles
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    fun openProfileScreen(profile: Profile?) {
        mainNavigator.openProfile(profile)
    }

    companion object {
        private const val TAG = "database"
    }
}
