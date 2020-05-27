package com.petapp.profiles.data

import com.petapp.core_api.database.entity.ProfileEntity
import com.petapp.profiles.domain.Profile

class ProfileEntityMapper {

    fun transformToProfile(profileEntity: ProfileEntity): Profile {
        return Profile(id = profileEntity.id, name = profileEntity.name, color = profileEntity.color, photo = profileEntity.photo)
    }

    fun transformToProfileEntity(profile: Profile): ProfileEntity {
        return ProfileEntity(id = profile.id, name = profile.name, color = profile.color, photo = profile.photo)
    }

    fun transformToProfile(profileEntities: List<ProfileEntity>): List<Profile> {
        val profiles = arrayListOf<Profile>()
        for (profileEntity in profileEntities) {
            val profile = transformToProfile(profileEntity)
            profiles.add(profile)
        }
        return profiles
    }
}