package com.petapp.capybara.data

import com.petapp.capybara.data.model.ItemHealthDiary
import io.reactivex.Single

interface HealthDiaryRepository {

    fun getItemsHealthDiary(): Single<List<ItemHealthDiary>>
}
