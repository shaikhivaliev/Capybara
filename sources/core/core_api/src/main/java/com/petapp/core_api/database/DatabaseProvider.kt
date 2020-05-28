package com.petapp.core_api.database

interface DatabaseProvider {

    fun provideDatabase(): DatabaseContract

    fun provideDao(): AppDao

}