package com.petapp.profiles.presentation.profiles

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.profiles.domain.Profile
import com.petapp.profiles.domain.ProfileRepository
import com.petapp.profiles.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import javax.inject.Inject

class ProfilesViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val router: Router
) : BaseViewModel() {

    var profiles = MutableLiveData<List<Profile>>()
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