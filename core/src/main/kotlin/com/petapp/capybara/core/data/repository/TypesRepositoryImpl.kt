package com.petapp.capybara.core.data.repository

import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.core.data.toType
import com.petapp.capybara.core.data.toTypeEntity
import com.petapp.capybara.core.database.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TypesRepositoryImpl(
    private val appDao: AppDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TypesRepository {

    override suspend fun getTypes(): List<Type> {
        return withContext(dispatcher) {
            appDao.getTypesWithSurveys().map { it.toType() }
        }
    }

    override suspend fun getType(typeId: Long): Type {
        return withContext(dispatcher) {
            appDao.getType(typeId).toType()
        }
    }

    override suspend fun createType(type: Type) {
        return withContext(dispatcher) {
            appDao.createType(type.toTypeEntity())
        }
    }

    override suspend fun deleteType(typeId: Long) {
        return withContext(dispatcher) {
            appDao.deleteType(typeId)
        }
    }
}
