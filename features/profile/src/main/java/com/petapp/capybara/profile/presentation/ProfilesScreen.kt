package com.petapp.capybara.profile.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.list.BaseLazyColumn
import com.petapp.capybara.list.IconTitleDescItem
import com.petapp.capybara.profile.R
import com.petapp.capybara.profile.di.ProfileComponentHolder
import com.petapp.capybara.profile.toUiData
import com.petapp.capybara.state.EmptyState
import com.petapp.capybara.state.ErrorState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfilesScreen(
    openNewProfile: () -> Unit,
    openProfileScreen: (Long) -> Unit
) {
    val vm: ProfilesVm = ProfileComponentHolder.component.provideProfilesVm()
    val profileState = vm.profilesState.collectAsState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openNewProfile() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                    contentDescription = null
                )
            }
        },
        content = {
            when (val state = profileState.value) {
                DataState.EMPTY -> EmptyState(stringResource(R.string.profile_mock))
                is DataState.ERROR -> ErrorState()
                is DataState.DATA -> ShowProfiles(
                    state = state.data,
                    openProfileScreen = { openProfileScreen(it.id) })
                else -> { // nothing
                }
            }
        }
    )
}

@Composable
private fun ShowProfiles(
    state: List<Profile>,
    openProfileScreen: (Profile) -> Unit
) {
    BaseLazyColumn {
        items(state) { item ->
            IconTitleDescItem(
                onClick = { openProfileScreen(item) },
                item = item.toUiData(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
