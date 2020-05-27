package com.petapp.calendar.data

import com.petapp.capybara.calendar.domain.Mark
import com.petapp.core_api.database.entity.ProfileEntity

class CalendarEntityMapper {

    private fun transformToMark(profileEntity: ProfileEntity): Mark {
        return Mark(id = profileEntity.id, name = profileEntity.name, color = profileEntity.color)
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