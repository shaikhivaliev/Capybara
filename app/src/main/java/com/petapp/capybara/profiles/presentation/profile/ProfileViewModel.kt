package com.petapp.capybara.profiles.presentation.profile

import android.util.Log
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.profiles.data.ProfileDataRepository
import com.petapp.capybara.profiles.domain.Profile
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.terrakok.cicerone.Router

class ProfileViewModel(
    private val repository: ProfileDataRepository,
    private val router: Router
) : BaseViewModel() {

    fun insertProfile(profile: Profile) {
        repository.insertProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("database", "${profile.id} insert success")
                },
                { Log.d("database", "${profile.id} insert error") }
            ).connect()
    }

    fun deleteProfile(profileId: Int) {
        repository.deleteProfile(profileId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("database", "$profileId delete success")
                },
                { Log.d("database", "$profileId delete error") }
            ).connect()
    }

    private fun updateProfile(profile: Profile) {
        repository.updateProfile(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("database", "${profile.id} update success")
                },
                { Log.d("database", "${profile.id} update error") }
            ).connect()
    }

}