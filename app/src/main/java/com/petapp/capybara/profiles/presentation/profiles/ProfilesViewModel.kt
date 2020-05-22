package com.petapp.capybara.profiles.presentation.profiles

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.profiles.domain.Profile
import com.petapp.capybara.profiles.domain.ProfileRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

class ProfilesViewModel(
    private val repository: ProfileRepository,
    private val router: Router
) : BaseViewModel() {

    var profiles = MutableLiveData<MutableList<Profile>>()
    var isShowMock = MutableLiveData<Boolean>()

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
                { Log.d("database", "Get profiles error") }
            ).connect()
    }

    fun navigateTo(profile: Screen) {
        router.navigateTo(profile)
    }
}