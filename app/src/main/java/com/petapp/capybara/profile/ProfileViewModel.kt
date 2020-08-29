package com.petapp.capybara.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.ProfileRepository
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.extensions.createImageFile
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _imageFile = MutableLiveData<File>()
    val imageFile: LiveData<File> get() = _imageFile


    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    fun getProfile(profileId: String) {
        repository.getProfile(profileId)
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
            repository.createProfile(profile)
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
            repository.updateProfile(profile)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        openProfilesScreen()
                        Log.d(TAG, "update profile ${profile.id} success")
                    },
                    {
                        _errorMessage.value = R.string.error_update_profile
                        Log.d(TAG, "update profile ${profile.id} error")
                    }
                ).connect()
        }
    }

    fun deleteProfile(profileId: String) {
        repository.deleteProfile(profileId)
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
        ProfileFragmentDirections.toProfiles().navigateWith(navController)
    }

    fun back() {
        navController.popBackStack()
    }

    companion object {
        private const val TAG = "database_profile"
    }
}