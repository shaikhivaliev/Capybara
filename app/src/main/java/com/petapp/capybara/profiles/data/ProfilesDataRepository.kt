package com.petapp.capybara.profiles.data

import com.petapp.capybara.database.AppDao
import com.petapp.capybara.profiles.domain.Profile
import com.petapp.capybara.profiles.domain.ProfilesRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilesDataRepository @Inject constructor(
    private val appDao: AppDao
) : ProfilesRepository {

    override fun getProfiles(): Single<MutableList<Profile>> {
        return appDao.getProfiles().map { it.transformToDomainList() }
    }

    override fun insertProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.insertProfile(profile.transformToData()) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateProfile(profile: Profile): Completable {
        return Completable.fromAction { appDao.updateProfile(profile.transformToData()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProfile(profileId: Int): Completable {
        return Completable.fromAction { appDao.deleteProfile(profileId) }
            .subscribeOn(Schedulers.io())
    }

    private fun ProfileEntity.transformToDomain(): Profile {
        return Profile(id = this.id, title = this.title, color = this.color)
    }

    private fun Profile.transformToData(): ProfileEntity {
        return ProfileEntity(id = this.id, title = this.title, color = this.color)
    }

    private fun MutableList<ProfileEntity>.transformToDomainList(): MutableList<Profile> {
        val profiles = arrayListOf<Profile>()
        for (profileEntity in this) {
            val profile = profileEntity.transformToDomain()
            profile.let { profiles.add(it) }
        }
        return profiles
    }

}