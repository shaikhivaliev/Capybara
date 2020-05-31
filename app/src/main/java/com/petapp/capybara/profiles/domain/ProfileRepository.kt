package com.petapp.capybara.profiles.domain

import com.petapp.capybara.profiles.domain.dto.Profile
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileRepository {

    fun getProfiles(): Single<List<Profile>>

    fun getProfile(profileId: String): Single<Profile>

    fun createProfile(profile: Profile): Completable

    fun updateProfile(profile: Profile): Completable

    fun deleteProfile(profileId: String): Completable

}