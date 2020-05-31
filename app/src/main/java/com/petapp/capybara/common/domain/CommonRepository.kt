package com.petapp.capybara.common.domain

import com.petapp.capybara.common.domain.dto.Mark
import io.reactivex.Single

interface CommonRepository {

    fun getMarks(): Single<List<Mark>>

}