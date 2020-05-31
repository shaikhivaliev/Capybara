package com.petapp.capybara.profiles.presentation.profiles

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.profiles.domain.ProfileRepository
import com.petapp.capybara.profiles.domain.dto.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfilesViewModel(
    private val repository: ProfileRepository) : BaseViewModel() {

    var profiles = MutableLiveData<List<Profile>>()
    var isShowMock = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()

    fun getProfiles() {
        repository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isShowMock.value = it.isEmpty()
                    profiles.value = it
                    Log.d("database", "Get profiles success")
                },
                {
                    errorMessage.value = "Error"
                    Log.d("database", "Get profiles error")
                }
            ).connect()
    }

}