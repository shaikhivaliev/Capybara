package com.petapp.capybara.profiles.data

import com.petapp.capybara.profiles.domain.Profile

class ProfileEntityMapper {

    private fun transformToDomain(profileEntity: ProfileEntity): Profile {
        return Profile(id = profileEntity.id, name = profileEntity.name, color = profileEntity.color, photo = profileEntity.photo)
    }

    fun transformToData(profile: Profile): ProfileEntity {
        return ProfileEntity(id = profile.id, name = profile.name, color = profile.color, photo = profile.photo)
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