package com.petapp.capybara.data

import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.database.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IProfileRepository {

    override suspend fun getProfiles(): List<Profile> {
        return withContext(dispatcher) {
            appDao.getProfilesWithSurveys().map { it.toProfile() }
        }
    }

    override suspend fun getProfile(profileId: Long): Profile {
        return withContext(dispatcher) {
            appDao.getProfile(profileId).toProfile()
        }
    }

    override suspend fun createProfile(profile: Profile) {
        return withContext(dispatcher) {
            appDao.createProfile(profile.toProfileEntity())
        }
    }

    override suspend fun updateProfile(profile: Profile) {
        return withContext(dispatcher) {
            appDao.updateProfile(profile.toProfileEntity())
        }
    }

    override suspend fun deleteProfile(profileId: Long) {
        return withContext(dispatcher) {
            appDao.deleteProfile(profileId)
        }
    }
}
