package com.petapp.capybara.data

import com.petapp.capybara.data.model.Profile

interface IProfileRepository {

    suspend fun getProfiles(): List<Profile>

    suspend fun getProfile(profileId: Long): Profile

    suspend fun createProfile(profile: Profile)

    suspend fun updateProfile(profile: Profile)

    suspend fun deleteProfile(profileId: Long)
}
