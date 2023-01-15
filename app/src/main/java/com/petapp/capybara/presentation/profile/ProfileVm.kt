package com.petapp.capybara.presentation.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.IMainCoordinator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.IHealthDiaryRepository
import com.petapp.capybara.data.IProfileRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.healthDiary.HealthDiaryForProfile
import com.petapp.capybara.extensions.createImageFile
import com.petapp.capybara.presentation.toPresentationModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class ProfileVmFactory(
    private val mainNavigator: IMainCoordinator,
    private val profileRepository: IProfileRepository,
    private val healthDiaryRepository: IHealthDiaryRepository
) : SavedStateVmAssistedFactory<ProfileVm> {
    override fun create(handle: SavedStateHandle) =
        ProfileVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            profileRepository = profileRepository,
            healthDiaryRepository = healthDiaryRepository
        )
}

class ProfileVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainCoordinator,
    private val profileRepository: IProfileRepository,
    private val healthDiaryRepository: IHealthDiaryRepository
) : BaseViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _imageFile = MutableLiveData<File>()
    val imageFile: LiveData<File> get() = _imageFile

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    private val _healthDiaryForProfile = MutableLiveData<HealthDiaryForProfile>()
    val healthDiaryForProfile: LiveData<HealthDiaryForProfile> = _healthDiaryForProfile

    fun getProfile(profileId: Long) {
        profileRepository.getProfile(profileId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _profile.value = it
                Log.d(TAG, "get profile ${it.id} success")
            },
                {
                    _errorMessage.value = R.string.error_get_profile
                    Log.d(TAG, "get profile error")
                }
            ).connect()
    }

    fun createProfile(profile: Profile?) {
        if (profile != null) {
            profileRepository.createProfile(profile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        openProfilesScreen()
                        Log.d(TAG, "create profile ${profile.id} success")
                    },
                    {
                        _errorMessage.value = R.string.error_create_profile
                        Log.d(TAG, "create profile ${profile.id} error")
                    }
                ).connect()
        }
    }

    fun updateProfile(profile: Profile?) {
        if (profile != null) {
            profileRepository.updateProfile(profile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        getProfile(profile.id)
                        Log.d(TAG, "update profile ${profile.id} success")
                    },
                    {
                        _errorMessage.value = R.string.error_update_profile
                        Log.d(TAG, "update profile ${profile.id} error")
                    }
                ).connect()
        }
    }

    fun deleteProfile(profileId: Long) {
        profileRepository.deleteProfile(profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    openProfilesScreen()
                    Log.d(TAG, "delete profile $profileId success")
                },
                {
                    _errorMessage.value = R.string.error_delete_profile
                    Log.d(TAG, "delete profile error")
                }
            ).connect()
    }

    fun getHealthDiaryItems(profileId: Long) {
        healthDiaryRepository.getItemsHealthDiary()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _healthDiaryForProfile.value = it.toPresentationModel(profileId)
                    Log.d(TAG, "get health diary items success")
                },
                {
                    _errorMessage.value = R.string.error_get_health_diary_items
                    Log.d(TAG, "get health diary items error")
                }
            ).connect()
    }

    fun createImageFile(context: Context) {
        Single.just(context.createImageFile())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _imageFile.value = it
                    Log.d(TAG, "create file success")
                },
                {
                    Log.d(TAG, "create file error")
                }
            ).connect()
    }

    private fun openProfilesScreen() {
        mainNavigator.openProfiles()
    }

    fun openHealthDiaryScreen(profileId: Long?) {
        mainNavigator.openHealthDiary(profileId)
    }

    fun back() {
        mainNavigator.back()
    }

    companion object {
        private const val TAG = "database_profile"
    }
}
