package com.petapp.capybara.data

import com.petapp.capybara.data.model.Profile
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileRepository {

    fun getProfiles(): Single<List<Profile>>

    fun getProfile(profileId: String): Single<Profile>

    fun createProfile(profile: Profile): Completable

    fun updateProfile(profile: Profile): Completable

    fun deleteProfile(profileId: String): Completable

}