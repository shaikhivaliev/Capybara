package com.petapp.capybara.calendar

import com.petapp.capybara.profiles.domain.Profile
import io.reactivex.Single

interface ProfilesRepository {

    fun profiles(): Single<List<Profile>>
    fun profile(profileId: Int): Single<Profile>

}