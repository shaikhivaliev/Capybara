package com.petapp.capybara.data

import com.petapp.capybara.data.model.Type
import com.petapp.capybara.database.AppDao
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TypesRepositoryImpl(private val appDao: AppDao) : TypesRepository {

    override fun getTypes(): Single<List<Type>> {
        return appDao.getTypes().map { it.toTypes() }
    }

    override fun getType(typeId: String): Single<Type> {
        return appDao.getType(typeId).map { it.toTypes() }
    }

    override fun createType(type: Type): Completable {
        return Completable.fromAction { appDao.insertType(type.toTypeEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateType(type: Type): Completable {
        return Completable.fromAction { appDao.updateType(type.toTypeEntity()) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteType(typeId: String): Completable {
        return Completable.fromAction { appDao.deleteType(typeId) }
            .subscribeOn(Schedulers.io())
    }
}