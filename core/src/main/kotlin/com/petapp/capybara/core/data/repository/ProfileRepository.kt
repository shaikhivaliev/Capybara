package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.Profile

interface ProfileRepository {

    suspend fun getProfiles(): List<Profile>

    suspend fun getProfile(profileId: Long): Profile

    suspend fun createProfile(profile: Profile)

    suspend fun updateProfile(profile: Profile)

    suspend fun deleteProfile(profileId: Long)
}
