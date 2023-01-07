package com.petapp.capybara.presentation.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.SideEffect
import com.petapp.capybara.core.navigation.ProfileNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.ImagePicker
import com.petapp.capybara.data.model.ImagePickerType
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.ui.Error
import com.petapp.capybara.ui.ShowSnackbar
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ProfileVmFactory

    private val vm: ProfileVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: ProfileNavDto? by navDto()

//    ImagePickerType.CAMERA -> vm.createImageFile(requireActivity())
//    ImagePickerType.GALLERY -> imageFromGallery.launch("image/*")
//    imageFile.observe(viewLifecycleOwner) { file ->
//        currentPhotoUri = FileProvider.getUriForFile(
//            requireActivity(),
//            requireActivity().getString(R.string.autority),
//            file
//        ).toString()
//        imageFromCamera.launch(Uri.parse(currentPhotoUri))


    private val imageFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
//            if (success) {
//                Glide.with(this)
//                    .load(currentPhotoUri)
//                    .centerCrop()
//                    .into(viewBinding.photo)
//            } else {
//                currentPhotoUri = args?.profile?.photo
//            }
        }

    private val camera = ImagePicker(
        ImagePickerType.CAMERA.ordinal.toLong(),
        ImagePickerType.CAMERA,
        R.string.profile_image_picker_camera,
        R.drawable.ic_camera
    )

    private val gallery = ImagePicker(
        ImagePickerType.GALLERY.ordinal.toLong(),
        ImagePickerType.GALLERY,
        R.string.profile_image_picker_gallery,
        R.drawable.ic_gallery
    )

    private val imageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.apply {
//            currentPhotoUri = this.toString()
//            Glide.with(requireActivity())
//                .load(this)
//                .centerCrop()
//                .into(viewBinding.photo)
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
        val profileState by vm.profileState.observeAsState()
        val sideEffect by vm.sideEffect.observeAsState()
        val profileInputData = ProfileInputData()

        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                ShowFab(profileState, profileInputData)
            },
            content = {
                when (val state = profileState) {
                    is DataState.DATA -> {}
                    is DataState.ERROR -> Error()
                    else -> { // nothing
                    }
                }
            }
        )
        if (sideEffect is SideEffect.ACTION) {
            // todo fix repeat showing
            ShowSnackbar(
                scaffoldState = scaffoldState,
                errorMessage = stringResource(R.string.error_empty_data)
            )
        }
    }

    @Composable
    private fun ShowFab(surveyState: DataState<ProfileMode>?, profileInputData: ProfileInputData) {
        surveyState?.onData { mode ->
            FloatingActionButton(
                onClick = {
                    when (mode) {
                        is ProfileMode.NEW, is ProfileMode.EDIT -> vm.verifyProfile()
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


    private fun deleteProfile(title: String, profileId: Long) {
        MaterialDialog(requireActivity()).show {
            if (title.isNotBlank()) {
                title(text = getString(R.string.profile_delete_explanation, title))
            } else {
                title(text = getString(R.string.profile_delete_explanation_empty))
            }
            positiveButton {
                vm.deleteProfile(profileId)
                cancel()
            }
            negativeButton { cancel() }
        }
    }
}
