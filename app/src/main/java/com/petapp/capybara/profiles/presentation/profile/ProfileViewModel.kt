package com.petapp.capybara.profiles.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.profiles.domain.ProfileRepository
import com.petapp.capybara.profiles.domain.dto.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfileViewModel(
    private val repository: ProfileRepository
) : BaseViewModel() {

    val profile = MutableLiveData<Profile>()
    val isChangeDone = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun getProfile(profileId: String) {
        repository.getProfile(profileId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("database", "get profile ${it.id} success")
                profile.value = it
            },
                {
                    Log.d("database", "get profile error")
                }
            ).connect()
    }

    fun createProfile(profile: Profile) {
        repository.createProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "create profile ${profile.id} success")
                },
                {
                    isChangeDone.value = false
                    errorMessage.value = "Insert error"
                    Log.d("database", "creare profile ${profile.id} error")
                }
            ).connect()
    }

    fun deleteProfile(profileId: String) {
        repository.deleteProfile(profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "delete profile $profileId success")
                },
                {
                    errorMessage.value = "error"
                    Log.d("database", "delete profile error")
                }
            ).connect()
    }

    fun updateProfile(profile: Profile) {
        repository.updateProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "update profile ${profile.id} success")
                },
                {
                    isChangeDone.value = false
                    errorMessage.value = "Update error"
                    Log.d("database", "update profile ${profile.id} error")
                }
            ).connect()
    }

}