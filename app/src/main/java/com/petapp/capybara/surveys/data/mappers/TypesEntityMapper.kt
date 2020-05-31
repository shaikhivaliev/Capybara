package com.petapp.capybara.surveys.data.mappers

import com.petapp.capybara.database.entity.TypeEntity
import com.petapp.capybara.surveys.domain.dto.Type

class TypesEntityMapper {

    fun transformToType(typeEntity: TypeEntity): Type {
        return Type(
            id = typeEntity.id,
            name = typeEntity.name,
            amount = typeEntity.amount,
            icon = typeEntity.icon
        )
    }

    fun transformToTypeEntity(type: Type): TypeEntity {
        return TypeEntity(
            id = type.id,
            name = type.name,
            amount = type.amount,
            icon = type.icon
        )
    }

    fun transformToType(typeEntities: List<TypeEntity>): List<Type> {
        val types = arrayListOf<Type>()
        for (typeEntity in typeEntities) {
            val profile = transformToType(typeEntity)
            types.add(profile)
        }
        return types
    }
}