package com.petapp.capybara.data.model

data class Months(
    val previousMonthSurveys: List<Survey>,
    val currentMonthSurveys: List<Survey>,
    val nextMonthSurveys: List<Survey>
)
