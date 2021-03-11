package com.petapp.capybara.data.model

data class Months(
    val twoMonthAgoSurveys: List<Survey>,
    val previousMonthSurveys: List<Survey>,
    val currentMonthSurveys: List<Survey>,
    val nextMonthSurveys: List<Survey>,
    val nextTwoMonthSurveys: List<Survey>
)
