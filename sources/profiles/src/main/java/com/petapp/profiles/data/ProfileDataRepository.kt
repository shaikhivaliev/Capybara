package com.petapp.profiles.data

import com.petapp.core_api.database.AppDao
import com.petapp.profiles.domain.Profile
import com.petapp.profiles.domain.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileDataRepository @Inject constructor(
    private val appDao: AppDao,
    private val mapper: ProfileEntityMapper
) : ProfileRepository {

    override fun getProfiles(): Single<List<Profile>> {
        return appDao.getProfiles().map(mapper::transformToProfile)
    }

    override fun insertProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.insertProfile(mapper.transformToProfileEntity(profile)) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.updateProfile(mapper.transformToProfileEntity(profile)) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProfile(profileId: String): Completable {
        return Completable.fromAction { appDao.deleteProfile(profileId) }
            .subscribeOn(Schedulers.io())
    }

    override fun getProfile(profileId: String): Single<Profile> {
        return appDao.getProfile(profileId).map(mapper::transformToProfile)
    }

}