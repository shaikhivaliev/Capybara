package com.petapp.core_impl.database

import com.petapp.core_api.context.AppProvider
import com.petapp.core_api.database.DatabaseProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AppProvider::class],
    modules = [DatabaseModule::class]
)
interface DatabaseComponent : DatabaseProvider