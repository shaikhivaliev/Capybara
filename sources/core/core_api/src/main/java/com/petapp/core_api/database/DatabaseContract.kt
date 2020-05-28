package com.petapp.core_api.database

import com.petapp.core_api.database.AppDao

interface DatabaseContract {

    fun appDao(): AppDao
}