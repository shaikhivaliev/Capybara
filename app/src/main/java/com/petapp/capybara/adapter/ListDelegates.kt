package com.petapp.capybara.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.data.model.*
import kotlinx.android.synthetic.main.item_step_health_diary.view.*

fun itemHealthDiaryDelegate(onClick: (ItemHealthDiary) -> Unit) =
    adapterDelegateLayoutContainer<ItemHealthDiary, HealthDiary>(R.layout.item_step_health_diary) {
        itemView.setOnClickListener { onClick(item.apply { isExpanded = isExpanded.not() }) }
        bind {
            itemView.title.text = getString(
                when (item.type) {
                    HealthDiaryType.BLOOD_PRESSURE -> R.string.health_diary_blood_pressure_title
                    HealthDiaryType.BLOOD_GLUCOSE -> R.string.health_diary_blood_glucose_title
                    HealthDiaryType.PULSE -> R.string.health_diary_pulse_title
                    HealthDiaryType.HEIGHT -> R.string.health_diary_height_title
                    HealthDiaryType.WEIGHT -> R.string.health_diary_weight_title
                }
            )
        }
    }

fun emptySurveyHealthDiaryDelegate() =
    adapterDelegateLayoutContainer<EmptyItemHealthDiary, HealthDiary>(R.layout.item_empty_survey) {
        bind { }
    }

fun surveyHealthDiaryDelegate(onClick: (SubItemHealthDiary) -> Unit) =
    adapterDelegateLayoutContainer<SubItemHealthDiary, HealthDiary>(R.layout.item_survey_health_diary) {
        itemView.setOnClickListener { onClick(item) }
        bind { }
    }
