package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.toProfile
import com.petapp.capybara.core.data.toProfileEntity
import com.petapp.capybara.core.database.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProfileRepository {

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
