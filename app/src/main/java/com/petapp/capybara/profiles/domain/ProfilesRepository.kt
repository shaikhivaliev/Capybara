package com.petapp.capybara.profiles.domain

import io.reactivex.Completable
import io.reactivex.Single

interface ProfilesRepository {

    fun getProfiles(): Single<MutableList<Profile>>
    fun insertProfile(profile: Profile): Completable
    fun updateProfile(profile: Profile): Completable
    fun deleteProfile(profileId: Int): Completable

}