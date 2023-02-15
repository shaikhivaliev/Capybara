package com.petapp.capybara.profile

import android.annotation.SuppressLint
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
import com.petapp.capybara.core.mvi.SideEffect
import com.petapp.capybara.core.vm.viewModel
import com.petapp.capybara.list.IconTitleItem
import com.petapp.capybara.profile.di.ProfileComponentHolder
import com.petapp.capybara.state.ErrorState
import com.petapp.capybara.state.ShowSnackbar
import com.petapp.capybara.styles.neutralN40

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    openProfilesScreen: () -> Unit
) {
    val vm: ProfileVm = ProfileComponentHolder.getComponent()?.provideProfileVm()!!
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
                verifyProfile = { mode, input -> vm.verifyProfile(mode, input) },
                toEditMode = { vm.toEditMode(it) }
            )
        },
        content = {
            when (val state = profileState.value) {
                is DataState.DATA -> {
                    ShowProfile(state.data, input)
                }
                is DataState.ERROR -> ErrorState()
                else -> { // nothing
                }
            }
            if (sideEffect.value is SideEffect.ACTION) {
                ShowSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    errorMessage = stringResource(R.string.error_empty_data),
                    dismissed = { vm.dismissSnackbar() }
                )
            }
        }
    )
}

@Composable
private fun ShowFab(
    state: DataState<ProfileMode>?,
    input: ProfileInputData,
    verifyProfile: (ProfileMode, ProfileInputData) -> Unit,
    toEditMode: (ProfileUI) -> Unit
) {
    state?.onData { mode ->
        FloatingActionButton(
            onClick = {
                when (mode) {
                    is ProfileMode.NEW, is ProfileMode.EDIT -> verifyProfile(
                        mode,
                        input
                    )
                    is ProfileMode.READONLY -> toEditMode(mode.data)
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
private fun ShowProfile(mode: ProfileMode, profileInputData: ProfileInputData) {
    when (mode) {
        is ProfileMode.NEW -> {
            val photoUri = mode.data.photoUri
            if (photoUri != null) {
                profileInputData.photoUri.value = photoUri
            }
            ProfileContent(
                colors = mode.data.colors,
                input = profileInputData
            )
        }
        is ProfileMode.EDIT -> {
            with(mode.data) {
                profileInputData.photoUri.value = profile.photo
                profileInputData.name.value = profile.name
                profileInputData.color.value = profile.color
            }
            ProfileContent(
                colors = mode.data.colors,
                input = profileInputData,
                profileId = mode.data.profile.id
            )
        }
        is ProfileMode.READONLY -> ProfileContentReadOnly(mode.data)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileContent(
    colors: List<Int>,
    input: ProfileInputData,
    profileId: Long? = null
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
            startColorDialog(colors) {
                input.color.value = it
            }
        }
        IconTitleItem(
            icon = R.drawable.ic_camera,
            title = R.string.profile_photo
        ) {
            pickImageFromGallery()
        }
        if (profileId != null) {
            DeleteButton(
                title = R.string.profile_delete,
                onClick = {
                    deleteProfile(
                        title = input.name.value,
                        profileId = profileId
                    )
                }
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

private fun deleteProfile(title: String, profileId: Long) {
    // todo
//    MaterialDialog(requireActivity()).show {
//        title(text = getString(R.string.profile_delete_explanation, title))
//        positiveButton {
//            vm.deleteProfile(profileId)
//            cancel()
//        }
//        negativeButton { cancel() }
//    }
}

@SuppressLint("CheckResult")
private fun startColorDialog(colorsId: List<Int>, onClick: (Int) -> Unit) {
    // todo
//    val initialColor = 0
//    val colors = colorsId.map { ContextCompat.getColor(requireActivity(), it) }
//    MaterialDialog(requireActivity()).show {
//        title(R.string.profile_choose_color)
//        colorChooser(colors.toIntArray(), initialSelection = initialColor) { _, color ->
//            onClick(color)
//        }
//        negativeButton(R.string.cancel) { cancel() }
//    }
}

private fun pickImageFromGallery() {
    // todo
//    val imageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        uri?.apply {
//            vm.updatePhoto(this.toString())
//        }
//    }
//    imageFromGallery.launch("image/*")
}
