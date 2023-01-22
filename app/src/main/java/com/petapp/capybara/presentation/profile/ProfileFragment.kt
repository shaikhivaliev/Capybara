package com.petapp.capybara.presentation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.state.DataState
import com.petapp.capybara.core.state.SideEffect
import com.petapp.capybara.core.navigation.ProfileNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.ui.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ProfileVmFactory

    private val vm: ProfileVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: ProfileNavDto? by navDto()

    private val imageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.apply {
            vm.updatePhoto(this.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            vm.getProfile(args?.profile?.id)
            setContent {
                MdcTheme {
                    ProfileScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun ProfileScreen() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val profileState by vm.profileState.collectAsState()
        val sideEffect by vm.sideEffect.collectAsState()
        val profileInputData = ProfileInputData()

        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                ShowFab(profileState, profileInputData)
            },
            content = {
                when (val state = profileState) {
                    is DataState.DATA -> {
                        ShowProfile(state.data, profileInputData)
                    }
                    is DataState.ERROR -> Error()
                    else -> { // nothing
                    }
                }
                if (sideEffect is SideEffect.ACTION) {
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
    private fun ShowFab(surveyState: DataState<ProfileMode>?, profileInputData: ProfileInputData) {
        surveyState?.onData { mode ->
            FloatingActionButton(
                onClick = {
                    when (mode) {
                        is ProfileMode.NEW, is ProfileMode.EDIT -> vm.verifyProfile(
                            mode,
                            profileInputData
                        )
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
    private fun ShowProfile(mode: ProfileMode, profileInputData: ProfileInputData) {
        when (mode) {
            is ProfileMode.NEW -> {
                val photoUri = mode.data.photoUri
                if (photoUri != null) {
                    profileInputData.photoUri.value = photoUri
                }
                ProfileContent(
                    colors = mode.data.colors,
                    profileInputData = profileInputData
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
                    profileInputData = profileInputData,
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
        profileInputData: ProfileInputData,
        profileId: Long? = null
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
            Row {
                GlideImage(
                    model = profileInputData.photoUri.value,
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
                        .background(Color(profileInputData.color.value))
                )
            }
            OutlinedTextFieldOneLine(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = profileInputData.name.value,
                onValueChange = {
                    profileInputData.name.value = it
                },
                label = stringResource(R.string.name)
            )
            IconTitleItem(
                icon = R.drawable.ic_palette,
                title = R.string.profile_label
            ) {
                startColorDialog(colors) {
                    profileInputData.color.value = it
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
                            title = profileInputData.name.value,
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
        MaterialDialog(requireActivity()).show {
            title(text = getString(R.string.profile_delete_explanation, title))
            positiveButton {
                vm.deleteProfile(profileId)
                cancel()
            }
            negativeButton { cancel() }
        }
    }

    @SuppressLint("CheckResult")
    private fun startColorDialog(colorsId: List<Int>, onClick: (Int) -> Unit) {
        val initialColor = args?.profile?.color ?: 0
        val colors = colorsId.map { ContextCompat.getColor(requireActivity(), it) }
        MaterialDialog(requireActivity()).show {
            title(R.string.profile_choose_color)
            colorChooser(colors.toIntArray(), initialSelection = initialColor) { _, color ->
                onClick(color)
            }
            negativeButton(R.string.cancel) { cancel() }
        }
    }

    private fun pickImageFromGallery() {
        imageFromGallery.launch("image/*")
    }

    override fun onDestroy() {
        super.onDestroy()
        imageFromGallery.unregister()
    }
}
