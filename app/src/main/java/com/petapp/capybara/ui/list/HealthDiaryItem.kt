package com.petapp.capybara.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.petapp.capybara.R
import com.petapp.capybara.core.data.model.HealthDiaryType
import com.petapp.capybara.core.data.model.ItemHealthDiary
import com.petapp.capybara.core.data.model.SurveyHealthDiary
import com.petapp.capybara.ui.styles.textMedium
import com.petapp.capybara.ui.styles.textSmall

@Composable
fun HealthDiaryHeader(
    item: ItemHealthDiary,
    expand: () -> Unit,
    addNew: () -> Unit
) {
    val title = stringResource(
        when (item.type) {
            HealthDiaryType.BLOOD_PRESSURE -> R.string.health_diary_blood_pressure_title
            HealthDiaryType.BLOOD_GLUCOSE -> R.string.health_diary_blood_glucose_title
            HealthDiaryType.PULSE -> R.string.health_diary_pulse_title
            HealthDiaryType.HEIGHT -> R.string.health_diary_height_title
            HealthDiaryType.WEIGHT -> R.string.health_diary_weight_title
        }
    )
    Row(
        modifier = Modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = textMedium()
        )
        if (item.isExpanded) {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickable { addNew() },
                painter = painterResource(R.drawable.ic_add_health_diary),
                contentDescription = null
            )
        }
        // todo icon animation
        Icon(
            modifier = Modifier
                .clickable { expand() },
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = null
        )
    }
}

@Composable
fun HealthDiarySurveyItem(
    item: SurveyHealthDiary,
    deleteSurvey: (Long) -> Unit
) {
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Text(
            text = item.date,
            style = textSmall()
        )
        Text(
            text = item.time,
            style = textSmall(),
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = item.surveyValue,
            style = textSmall(),
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = item.unitOfMeasure,
            style = textSmall(),
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(0.5F)
        )
        Image(
            modifier = Modifier
                .weight(0.1F)
                .clickable { deleteSurvey(item.id) },
            painter = painterResource(R.drawable.ic_delete_small),
            contentDescription = null
        )
    }
}
