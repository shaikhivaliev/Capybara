package com.petapp.capybara.calendar

import com.petapp.capybara.Profile
import io.reactivex.Single

interface ProfilesRepository {

    fun profiles(): Single<List<Profile>>
    fun profile(profileId: Int): Single<Profile>

}