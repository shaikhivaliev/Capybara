package com.petapp.capybara.profiles

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.ProfileRepository
import com.petapp.capybara.data.model.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfilesViewModel(
    private val repository: ProfileRepository
) : BaseViewModel() {

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _isShowMock = MutableLiveData<Boolean>()
    val isShowMock: LiveData<Boolean> get() = _isShowMock

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    fun getProfiles() {
        repository.getProfiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isShowMock.value = it.isEmpty()
                    _profiles.value = it
                    Log.d(TAG, "get profiles success")
                },
                {
                    _errorMessage.value = R.string.error_get_profile
                    Log.d(TAG, "get profiles error")
                }
            ).connect()
    }

    companion object {
        private const val TAG = "database"
    }
}