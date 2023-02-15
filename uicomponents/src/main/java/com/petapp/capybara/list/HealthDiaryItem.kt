package com.petapp.capybara.list

import androidx.annotation.StringRes
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
import com.petapp.capybara.styles.textMedium
import com.petapp.capybara.styles.textSmall
import com.petapp.capybara.uicomponents.R

@Composable
fun HealthDiaryHeader(
    @StringRes title: Int,
    isExpanded: Boolean,
    expand: () -> Unit,
    addNew: () -> Unit
) {
    Row(
        modifier = Modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(title),
            style = textMedium()
        )
        if (isExpanded) {
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
    date: String,
    time: String,
    surveyValue: String,
    unitOfMeasure: String,
    id: Long,
    deleteSurvey: (Long) -> Unit
) {
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Text(
            text = date,
            style = textSmall()
        )
        Text(
            text = time,
            style = textSmall(),
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = surveyValue,
            style = textSmall(),
            modifier = Modifier.padding(start = 16.dp)
        )
        Text(
            text = unitOfMeasure,
            style = textSmall(),
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(0.5F)
        )
        Image(
            modifier = Modifier
                .weight(0.1F)
                .clickable { deleteSurvey(id) },
            painter = painterResource(R.drawable.ic_delete_small),
            contentDescription = null
        )
    }
}
