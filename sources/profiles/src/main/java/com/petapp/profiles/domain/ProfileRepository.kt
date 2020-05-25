package com.petapp.profiles.domain

import io.reactivex.Completable
import io.reactivex.Single

interface ProfileRepository {

    fun getProfiles(): Single<List<Profile>>

    fun getProfile(profileId: String): Single<Profile>

    fun insertProfile(profile: Profile): Completable

    fun updateProfile(profile: Profile): Completable

    fun deleteProfile(profileId: String): Completable

}