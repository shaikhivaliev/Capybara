package com.petapp.core_api.database.entity

import com.petapp.core_api.database.AppDao

interface DatabaseContract {

    fun appDao(): AppDao
}