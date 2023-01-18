package com.petapp.capybara.presentation.profile

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.SideEffect
import com.petapp.capybara.core.navigation.IMainNavigator
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

class ProfileVmFactory(
    private val mainNavigator: IMainNavigator,
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
    private val mainNavigator: IMainNavigator,
    private val profileRepository: IProfileRepository,
    private val healthDiaryRepository: IHealthDiaryRepository
) : BaseViewModel() {

    private val _profileState = MutableLiveData<DataState<ProfileMode>>()
    val profileState: LiveData<DataState<ProfileMode>> get() = _profileState

    private val _sideEffect = MutableLiveData<SideEffect>()
    val sideEffect: LiveData<SideEffect> get() = _sideEffect

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
            getProfileHealthDiary(profileId)
        }
    }

    private fun getProfileHealthDiary(profileId: Long) {
        Single.zip(
            profileRepository.getProfile(profileId)
                .subscribeOn(Schedulers.io()),
            healthDiaryRepository.getItemsHealthDiary()
                .subscribeOn(Schedulers.io())
        ) { profile, healthDiary ->
            ProfileUI(
                colors = COLORS,
                profile = profile,
                healthDiary = healthDiary.toPresentationModel(profileId)
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _profileState.value = DataState.DATA(
                    ProfileMode.READONLY(it)
                )
            },
                {
                    _profileState.value = DataState.ERROR(it)
                }
            ).connect()
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
            val profile = createProfile(mode, profileInputData)

            val request = when {
                mode is ProfileMode.NEW && profile != null -> {
                    profileRepository.createProfile(profile)
                }
                mode is ProfileMode.EDIT && profile != null -> {
                    profileRepository.updateProfile(profile)
                }
                else -> return
            }

            request
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    openProfilesScreen()
                }, {
                    _profileState.value = DataState.ERROR(it)
                }).connect()
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
        profileRepository.deleteProfile(profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    openProfilesScreen()
                },
                {
                    _profileState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    private fun openProfilesScreen() {
        mainNavigator.openProfiles()
    }

    fun dismissSnackbar() {
        _sideEffect.value = SideEffect.READY
    }

    fun updatePhoto(uri: String) {
        _profileState.value?.onData { mode ->
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

    fun createImageFile(context: Context) {
        Single.just(context.createImageFile())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    // _imageFile.value = it
                },
                {
                    _profileState.value = DataState.ERROR(it)
                }
            ).connect()
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
    val profile: Profile,
    val healthDiary: HealthDiaryForProfile
)

data class ProfileInputData(
    val photoUri: MutableState<String> = mutableStateOf(""),
    val name: MutableState<String> = mutableStateOf(""),
    val color: MutableState<Int> = mutableStateOf(0)
)
