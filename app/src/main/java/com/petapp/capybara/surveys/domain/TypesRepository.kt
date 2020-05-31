package com.petapp.capybara.surveys.domain

import com.petapp.capybara.surveys.domain.dto.Type
import io.reactivex.Completable
import io.reactivex.Single

interface TypesRepository {

    fun getTypes(): Single<List<Type>>

    fun getType(typeId: String): Single<Type>

    fun createType(type: Type): Completable

    fun updateType(type: Type): Completable

    fun deleteType(typeId: String): Completable
}
