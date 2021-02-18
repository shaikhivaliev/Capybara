package com.petapp.capybara.data

import com.petapp.capybara.data.model.Type
import io.reactivex.Completable
import io.reactivex.Single

interface TypesRepository {

    fun getTypes(): Single<List<Type>>

    fun getType(typeId: Long): Single<Type>

    fun createType(type: Type): Completable

    fun updateType(type: Type): Completable

    fun deleteType(typeId: Long): Completable
}
