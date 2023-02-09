package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.Type

interface TypesRepository {

    suspend fun getTypes(): List<Type>

    suspend fun getType(typeId: Long): Type

    suspend fun createType(type: Type)

    suspend fun deleteType(typeId: Long)
}
