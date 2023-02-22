package com.petapp.capybara.profile.presentation

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.petapp.capybara.DeleteButton
import com.petapp.capybara.OutlinedTextFieldOneLine
import com.petapp.capybara.OutlinedTextFieldReadOnly
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.dialogs.InfoDialog
import com.petapp.capybara.list.IconTitleItem
import com.petapp.capybara.profile.R
import com.petapp.capybara.profile.di.ProfileComponentHolder
import com.petapp.capybara.profile.state.ProfileEffect
import com.petapp.capybara.profile.state.ProfileInputData
import com.petapp.capybara.profile.state.ProfileMode
import com.petapp.capybara.profile.state.ProfileUI
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.state.ShowSnackbar
import com.petapp.capybara.styles.neutralN40
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    profileId: Long? = null,
    openProfilesScreen: () -> Unit
) {
    val vm: ProfileVm = ProfileComponentHolder.component.provideProfileVm()
    vm.getProfile(profileId)
    val profileState = vm.profileState.collectAsState()
    val sideEffect = vm.sideEffect.collectAsState()
    val input = ProfileInputData()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ShowFab(
                state = profileState.value,
                input = input,
                vm = vm
            )
        },
        content = {
            when (val state = profileState.value) {
                is DataState.DATA -> {
                    ShowProfile(
                        state = state.data,
                        input = input,
                        vm = vm
                    )
                }
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
            when (val effect = sideEffect.value) {
                ProfileEffect.ShowSnackbar -> {
                    ShowSnackbar(
                        snackbarHostState = scaffoldState.snackbarHostState,
                        errorMessage = stringResource(R.string.error_empty_data),
                        dismissed = { vm.setSideEffect(ProfileEffect.Ready) }
                    )
                }
                is ProfileEffect.ShowDeleteDialog -> {
                    InfoDialog(
                        title = R.string.profile_delete_explanation,
                        click = {
                            vm.deleteProfile(effect.profileId)
                        },
                        dismiss = { vm.setSideEffect(ProfileEffect.Ready) }
                    )
                }
                is ProfileEffect.NavigateToProfile -> openProfilesScreen()
                is ProfileEffect.ShowAddingColor -> UpdateColor(
                    choose = { vm.updateColor(it) },
                    dismiss = { vm.setSideEffect(ProfileEffect.Ready) }
                )
                is ProfileEffect.ShowAddingPhoto -> UpdatePhoto {
                    vm.updatePhoto(it)
                }
                else -> { // nothing
                }
            }
        }
    )
}

@Composable
private fun ShowFab(
    state: DataState<ProfileMode>?,
    input: ProfileInputData,
    vm: ProfileVm
) {
    state?.onData { mode ->
        FloatingActionButton(
            onClick = {
                when (mode) {
                    is ProfileMode.NEW, is ProfileMode.EDIT -> vm.verifyProfile(mode, input)
                    is ProfileMode.READONLY -> vm.toEditMode(mode.data)
                }
            }) {
            val fabImage = when (mode) {
                is ProfileMode.NEW -> ImageVector.vectorResource(R.drawable.ic_done)
                is ProfileMode.EDIT -> ImageVector.vectorResource(R.drawable.ic_done)
                is ProfileMode.READONLY -> ImageVector.vectorResource(R.drawable.ic_edit)
            }
            Icon(
                imageVector = fabImage,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ShowProfile(
    state: ProfileMode,
    input: ProfileInputData,
    vm: ProfileVm
) {
    when (state) {
        is ProfileMode.NEW -> {
            state.data.color?.let { input.color.value = it }
            state.data.photoUri?.let { input.photoUri.value = it }
            ProfileContent(
                input = input,
                vm = vm
            )
        }
        is ProfileMode.EDIT -> {
            with(state.data) {
                input.photoUri.value = profile.photo
                input.name.value = profile.name
                input.color.value = profile.color
            }
            ProfileContent(
                input = input,
                profileId = state.data.profile.id,
                vm = vm
            )
        }
        is ProfileMode.READONLY -> ProfileContentReadOnly(state.data)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileContent(
    input: ProfileInputData,
    profileId: Long? = null,
    vm: ProfileVm
) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        Row {
            GlideImage(
                model = input.photoUri.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .border(
                        color = neutralN40,
                        width = 1.dp
                    )
            ) {
                it
                    .placeholder(R.drawable.ic_launcher_foreground)
            }
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(input.color.value))
            )
        }
        OutlinedTextFieldOneLine(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = input.name.value,
            onValueChange = {
                input.name.value = it
            },
            label = stringResource(R.string.name)
        )
        IconTitleItem(
            icon = R.drawable.ic_palette,
            title = R.string.profile_label
        ) {
            vm.setSideEffect(ProfileEffect.ShowAddingColor)
        }
        IconTitleItem(
            icon = R.drawable.ic_camera,
            title = R.string.profile_photo
        ) {
            vm.setSideEffect(ProfileEffect.ShowAddingPhoto)
        }
        if (profileId != null) {
            DeleteButton(
                title = R.string.profile_delete,
                onClick = { vm.setSideEffect(ProfileEffect.ShowDeleteDialog(profileId)) }
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileContentReadOnly(data: ProfileUI) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        Row {
            GlideImage(
                model = data.profile.photo,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .border(
                        color = neutralN40,
                        width = 1.dp
                    )
            ) {
                it
                    .placeholder(R.drawable.ic_launcher_foreground)
            }
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(data.profile.color))
            )
        }
        OutlinedTextFieldReadOnly(
            value = data.profile.name,
            label = stringResource(R.string.name)
        )
    }
}

@SuppressLint("CheckResult")
@Composable
private fun UpdateColor(choose: (Int) -> Unit, dismiss: () -> Unit) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        onCloseRequest = { dismiss() },
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok") {
                dismiss()
                dialogState.hide()
            }
        }
    ) {
        title(stringResource(R.string.profile_choose_color))
        colorChooser(ProfileVm.COLORS.map { it.first }, initialSelection = 0) { color ->
            ProfileVm.COLORS.map { if (it.first == color) choose(it.second) }
        }
    }
    dialogState.show()
}

@Composable
fun UpdatePhoto(update: (String) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        update(it.toString())
    }
    launcher.launch("image/*")
}
