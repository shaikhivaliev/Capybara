package com.petapp.capybara.data

import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.database.AppDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProfileRepositoryImpl(private val appDao: AppDao) : ProfileRepository {

    override fun getProfiles(): Single<List<Profile>> {
        return appDao.getProfiles().map { it.toProfiles() }
    }

    override fun getProfile(profileId: String): Single<Profile> {
        return appDao.getProfile(profileId).map { it.toProfile() }
    }

    override fun createProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.createProfile(profile.toProfileEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.updateProfile(profile.toProfileEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProfile(profileId: String): Completable {
        return Completable.fromAction { appDao.deleteProfile(profileId) }
            .subscribeOn(Schedulers.io())
    }
}