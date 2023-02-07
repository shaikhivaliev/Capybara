package com.petapp.capybara.data

import com.petapp.capybara.data.model.Type

interface ITypesRepository {

    suspend fun getTypes(): List<Type>

    suspend fun getType(typeId: Long): Type

    suspend fun createType(type: Type)

    suspend fun deleteType(typeId: Long)
}
