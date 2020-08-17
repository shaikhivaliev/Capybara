package com.petapp.capybara.data

import com.petapp.capybara.data.model.Mark
import io.reactivex.Single

interface MarksRepository {

    fun getMarks(): Single<List<Mark>>
}