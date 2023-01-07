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
                        colors = COLORS,
                        chooseOptions = CHOOSE_OPTIONS
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
                chooseOptions = CHOOSE_OPTIONS,
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

    fun verifyProfile() {
    }

    fun createProfile(profile: Profile?) {
        if (profile != null) {
            profileRepository.createProfile(profile)
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
    }

    fun updateProfile(profile: Profile?) {
        if (profile != null) {
            profileRepository.updateProfile(profile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        getProfile(profile.id)
                    },
                    {
                        _profileState.value = DataState.ERROR(it)
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
                },
                {
                    _profileState.value = DataState.ERROR(it)
                }
            ).connect()
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

    private fun openProfilesScreen() {
        mainNavigator.openProfiles()
    }

    fun openHealthDiaryScreen(profileId: Long?) {
        mainNavigator.openHealthDiary(profileId ?: 0L)
    }

    fun back() {
        mainNavigator.back()
    }

    fun dismissSnackbar() {
        _sideEffect.value = SideEffect.READY
    }

    companion object {
        private val COLORS: List<String> = listOf(
//        ContextCompat.getColor(requireActivity(), R.color.red_500),
//        ContextCompat.getColor(requireActivity(), R.color.pink_500),
//        ContextCompat.getColor(requireActivity(), R.color.deep_purple_500),
//        ContextCompat.getColor(requireActivity(), R.color.indigo_500),
//        ContextCompat.getColor(requireActivity(), R.color.light_blue_500),
//        ContextCompat.getColor(requireActivity(), R.color.cyan_500),
//        ContextCompat.getColor(requireActivity(), R.color.teal_500),
//        ContextCompat.getColor(requireActivity(), R.color.green_500),
//        ContextCompat.getColor(requireActivity(), R.color.lime_500),
//        ContextCompat.getColor(requireActivity(), R.color.yellow_500),
//        ContextCompat.getColor(requireActivity(), R.color.amber_500),
//        ContextCompat.getColor(requireActivity(), R.color.deep_orange_500)
        )
        private val CHOOSE_OPTIONS: List<Int> = listOf(
            R.string.profile_image_picker_camera,
            R.string.profile_image_picker_gallery,
            R.string.profile_image_picker_delete_image
        )
    }
}

sealed class ProfileMode {
    data class NEW(val data: ProfileNew) : ProfileMode()
    data class EDIT(val data: ProfileUI) : ProfileMode()
    data class READONLY(val data: ProfileUI) : ProfileMode()
}

data class ProfileNew(
    val colors: List<String>,
    val chooseOptions: List<Int>
)

data class ProfileUI(
    val colors: List<String>,
    val chooseOptions: List<Int>,
    val profile: Profile,
    val healthDiary: HealthDiaryForProfile
)

data class ProfileInputData(
    val photoUri: MutableState<String> = mutableStateOf(""),
    val name: MutableState<String> = mutableStateOf(""),
    val color: MutableState<Int> = mutableStateOf(0)
)
