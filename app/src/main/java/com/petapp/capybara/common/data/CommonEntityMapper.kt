package com.petapp.capybara.common.data

import com.petapp.capybara.common.domain.dto.Mark
import com.petapp.capybara.database.entity.ProfileEntity

class CommonEntityMapper {

    private fun transformToMark(profileEntity: ProfileEntity): Mark {
        return Mark(
            id = profileEntity.id,
            name = profileEntity.name,
            color = profileEntity.color
        )
    }

    fun transformToMark(profileEntities: List<ProfileEntity>): List<Mark> {
        val marks = arrayListOf<Mark>()
        for (profileEntity in profileEntities) {
            val mark = transformToMark(profileEntity)
            marks.add(mark)
        }
        return marks
    }
}