package com.petapp.capybara.profiles.data

import com.petapp.capybara.database.AppDao
import com.petapp.capybara.profiles.domain.Profile
import com.petapp.capybara.profiles.domain.ProfilesRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProfilesDataRepository(private val appDao: AppDao, private val mapper: ProfileEntityMapper) : ProfilesRepository {

    override fun getProfiles(): Single<MutableList<Profile>> {
        return appDao.getProfiles().map(mapper::transformToDomain)
    }

    override fun insertProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.insertProfile(mapper.transformToData(profile)) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.updateProfile(mapper.transformToData(profile)) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProfile(profileId: Int): Completable {
        return Completable.fromAction { appDao.deleteProfile(profileId) }
            .subscribeOn(Schedulers.io())
    }
}