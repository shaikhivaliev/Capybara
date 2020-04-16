package com.petapp.capybara.model

import com.petapp.capybara.entity.Dataset

interface Repository {

    suspend fun getData(
        data: (Dataset) -> Unit,
        error: (String) -> Unit,
        inProgress: (Boolean) -> Unit
    )
}