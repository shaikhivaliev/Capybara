package com.petapp.profiles.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.core_api.navigation.FragmentProvider
import com.petapp.profiles.BaseViewModel
import com.petapp.profiles.domain.Profile
import com.petapp.profiles.domain.ProfileRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
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
                Log.d("database", "${it.id} get success")
                profile.value = it
            },
                {
                    Log.d("database", "get error")
                }
            ).connect()
    }

    fun insertProfile(profile: Profile) {
        repository.insertProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "${profile.id} insert success")
                },
                {
                    isChangeDone.value = false
                    errorMessage.value = "Insert error"
                    Log.d("database", "${profile.id} insert error")
                }
            ).connect()
    }

    fun deleteProfile() {
        profile.value?.let {
            repository.deleteProfile(it.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isChangeDone.value = true
                        Log.d("database", "${it.name} delete success")
                    },
                    {
                        isChangeDone.value = false
                        errorMessage.value = "Delete error"
                        Log.d("database", "delete error")
                    }
                ).connect()
        }
    }

    fun updateProfile(profile: Profile) {
        repository.updateProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "${profile.id} update success")
                },
                {
                    isChangeDone.value = false
                    errorMessage.value = "Update error"
                    Log.d("database", "${profile.id} update error")
                }
            ).connect()
    }

}