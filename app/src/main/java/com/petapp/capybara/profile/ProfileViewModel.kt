package com.petapp.capybara.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.ProfileRepository
import com.petapp.capybara.data.model.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _isChangeDone = MutableLiveData<Boolean>()
    val isChangeDone: LiveData<Boolean> get() = _isChangeDone

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

    fun createProfile(profile: Profile) {
        repository.createProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isChangeDone.value = true
                    Log.d("database", "create profile ${profile.id} success")
                },
                {
                    _isChangeDone.value = false
                    _errorMessage.value = R.string.error_create_profile
                    Log.d("database", "create profile ${profile.id} error")
                }
            ).connect()
    }

    fun deleteProfile(profileId: String) {
        repository.deleteProfile(profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isChangeDone.value = true
                    Log.d("database", "delete profile $profileId success")
                },
                {
                    _errorMessage.value = R.string.error_delete_profile
                    Log.d("database", "delete profile error")
                }
            ).connect()
    }

    fun updateProfile(profile: Profile) {
        repository.updateProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isChangeDone.value = true
                    Log.d(TAG, "update profile ${profile.id} success")
                },
                {
                    _isChangeDone.value = false
                    _errorMessage.value = R.string.error_update_profile
                    Log.d(TAG, "update profile ${profile.id} error")
                }
            ).connect()
    }

   fun back() {
        navController.popBackStack()
    }

    companion object {
        private const val TAG = "database"
    }
}