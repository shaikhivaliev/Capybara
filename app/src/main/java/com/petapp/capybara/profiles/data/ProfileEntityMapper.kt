package com.petapp.capybara.profiles.data

import com.petapp.capybara.profiles.domain.Profile

class ProfileEntityMapper {

    private fun transformToDomain(profileEntity: ProfileEntity): Profile {
        return Profile(id = profileEntity.id, title = profileEntity.title, color = profileEntity.color)
    }

    fun transformToData(profile: Profile): ProfileEntity {
        return ProfileEntity(id = profile.id, title = profile.title, color = profile.color)
    }

    fun transformToDomain(profileEntities: MutableList<ProfileEntity>): MutableList<Profile> {
        val profiles = arrayListOf<Profile>()
        for (profileEntity in profileEntities) {
            val profile = transformToDomain(profileEntity)
            profile.let { profiles.add(it) }
        }
        return profiles
    }
}