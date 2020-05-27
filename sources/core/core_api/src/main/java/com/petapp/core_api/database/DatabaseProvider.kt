package com.petapp.core_api.database

import com.petapp.core_api.database.entity.DatabaseContract

interface DatabaseProvider {

    fun provideDatabase(): DatabaseContract

    fun provideDao(): AppDao

}