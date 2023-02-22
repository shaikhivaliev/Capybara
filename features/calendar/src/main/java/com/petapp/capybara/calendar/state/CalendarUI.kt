package com.petapp.capybara.calendar.state

import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.data.model.Survey
import java.time.LocalDate

data class CalendarUI(
    val profiles: List<Profile>,
    val surveys: List<Survey>,
    val checkedProfileId: Long,
    val checkedSurveysDates: List<LocalDate>,
)
