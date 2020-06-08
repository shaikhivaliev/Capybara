package com.petapp.capybara.surveys.data

import com.petapp.capybara.database.AppDao
import com.petapp.capybara.surveys.data.mappers.TypesEntityMapper
import com.petapp.capybara.surveys.domain.TypesRepository
import com.petapp.capybara.surveys.domain.dto.Type
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TypesDataRepository(
    private val appDao: AppDao,
    private val mapper: TypesEntityMapper
) : TypesRepository {

    override fun getTypes(): Single<List<Type>> {
        return appDao.getTypes().map(mapper::transformToType)
    }

    override fun getType(typeId: String): Single<Type> {
        return appDao.getType(typeId).map(mapper::transformToType)
    }

    override fun createType(type: Type): Completable {
        return Completable.fromAction { appDao.insertType(mapper.transformToTypeEntity(type)) }
            .subscribeOn(Schedulers.io())
    }

    override fun updateType(type: Type): Completable {
        return Completable.fromAction { appDao.updateType(mapper.transformToTypeEntity(type)) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteType(typeId: String): Completable {
        return Completable.fromAction { appDao.deleteType(typeId) }
            .subscribeOn(Schedulers.io())
    }
}