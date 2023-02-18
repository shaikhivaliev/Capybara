package com.petapp.capybara.types

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.petapp.capybara.core.data.model.Type
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.list.BaseLazyColumn
import com.petapp.capybara.list.IconTitleDescItem
import com.petapp.capybara.state.EmptyState
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.styles.textMedium
import com.petapp.capybara.types.di.TypesComponentHolder

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TypesScreen(
    openHealthDiary: () -> Unit,
    openSurveysScreen: (Long) -> Unit
) {
    val vm: TypesVm = TypesComponentHolder.component.provideTypesVm()
    val typeState by vm.collectStore().collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        content = {
            Column {
                Card(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp), elevation = 4.dp
                ) {
                    Row(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { openHealthDiary() },
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Image(
                                painter = painterResource(R.drawable.ic_healt_diary),
                                contentDescription = null,
                                contentScale = ContentScale.Inside
                            )
                            Text(
                                text = stringResource(R.string.health_diary_health_diary),
                                style = textMedium(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        })
                }
                when (val state = typeState.state) {
                    DataState.EMPTY -> EmptyState(stringResource(R.string.profile_mock))
                    is DataState.ERROR -> ErrorState()
                    is DataState.DATA -> ShowTypes(
                        state = state.data,
                        openSurveysScreen = openSurveysScreen
                    )
                    else -> { // nothing
                    }
                }
            }
        }
    )
}

@Composable
private fun ShowTypes(
    state: List<Type>,
    openSurveysScreen: (Long) -> Unit
) {
    BaseLazyColumn {
        items(state) { item ->
            IconTitleDescItem(
                onClick = { openSurveysScreen(item.id) },
                item = item.toUiData(),
                contentScale = ContentScale.Inside
            )
        }
    }
}
