package com.petapp.capybara.data

import com.petapp.capybara.data.model.Type
import com.petapp.capybara.database.AppDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TypesRepository(private val appDao: AppDao) : ITypesRepository {

    override fun getTypes(): Single<List<Type>> {
        return appDao.getTypesWithSurveys().map { it.toTypes() }
    }

    override fun getType(typeId: Long): Single<Type> {
        return appDao.getType(typeId).map { it.toType() }
    }

    override fun createType(type: Type): Completable {
        return Completable.fromAction { appDao.createType(type.toTypeEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateType(type: Type): Completable {
        return Completable.fromAction { appDao.updateType(type.toTypeEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteType(typeId: Long): Completable {
        return Completable.fromAction { appDao.deleteType(typeId) }
            .subscribeOn(Schedulers.io())
    }
}
