package com.petapp.capybara.presentation.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.bumptech.glide.Glide
import com.petapp.capybara.R
import com.petapp.capybara.common.MarginItemDecoration
import com.petapp.capybara.data.model.DeleteImage
import com.petapp.capybara.data.model.ImagePicker
import com.petapp.capybara.data.model.ImagePickerType
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.databinding.FragmentProfileBinding
import com.petapp.capybara.extensions.hideKeyboard
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: ProfileFragmentArgs by navArgs()

    private var currentPhotoUri: String? = null

    private val currentColor = MutableLiveData<Int>()

    private val adapterImagePickerDialog: ImagePickerDialogAdapter = ImagePickerDialogAdapter(
        itemClick = { imagePicker ->
            when (imagePicker.type) {
                ImagePickerType.CAMERA -> viewModel.createImageFile(requireActivity())
                ImagePickerType.GALLERY -> imageFromGallery.launch("image/*")
            }
            imagePickerDialog?.cancel()
        },
        deleteImage = {}
    )

    private var imagePickerDialog: MaterialDialog? = null

    private val imageFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                Glide.with(this)
                    .load(currentPhotoUri)
                    .centerCrop()
                    .into(viewBinding.photo)
            } else {
                currentPhotoUri = args.profile?.photo
            }
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
            currentPhotoUri = this.toString()
            Glide.with(requireActivity())
                .load(this)
                .centerCrop()
                .into(viewBinding.photo)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()

        args.profile?.id?.apply {
            viewModel.getProfile(this)
            viewModel.getHealthDiaryItems(this)
        }
        if (args.profile?.id == null) {
            setEditMode(true)
            Glide.with(this)
                .load(R.drawable.ic_user_144dp)
                .centerInside()
                .into(viewBinding.photo)
            viewBinding.nameEt.requestFocus()
            viewBinding.nameEt.showKeyboard()
        }
    }

    private fun initViews() {

        viewBinding.edit.setOnClickListener {
            setEditMode(true)
            viewBinding.deleteProfile.isVisible = true
        }

        viewBinding.done.setOnClickListener {
            viewBinding.deleteProfile.isVisible = false
            if (args.profile != null) {
                viewBinding.done.hideKeyboard()
                viewModel.updateProfile(profileBuilder())
            } else {
                viewBinding.done.hideKeyboard()
                viewModel.createProfile(profileBuilder())
            }
        }

        viewBinding.nameEt.doAfterTextChanged { viewBinding.nameLayout.error = null }

        viewBinding.changePhoto.setOnClickListener { pickImages(listOf(camera, gallery)) }

        viewBinding.changeColor.setOnClickListener { startColorDialog() }

        viewBinding.deleteProfile.setOnClickListener { deleteProfile() }

        viewBinding.healthDiary.setOnClickListener { viewModel.openHealthDiaryScreen(args.profile?.id) }
    }

    private fun initObservers() {
        with(viewModel) {
            profile.observe(viewLifecycleOwner, { profile ->
                setProfileCard(profile)
                setEditMode(false)
            })

            imageFile.observe(viewLifecycleOwner, { file ->
                currentPhotoUri = FileProvider.getUriForFile(
                    requireActivity(),
                    requireActivity().getString(R.string.autority),
                    file
                ).toString()
                imageFromCamera.launch(Uri.parse(currentPhotoUri))
            })

            errorMessage.observe(viewLifecycleOwner, { error ->
                requireActivity().toast(error)
            })

            currentColor.observe(viewLifecycleOwner, { color ->
                viewBinding.colorMarker.setBackgroundColor(color)
            })

            healthDiaryForProfile.observe(viewLifecycleOwner, { profile ->
                viewBinding.bloodPressureValue.text =
                    profile.bloodPressure ?: getString(R.string.profile_item_health_diary_empty)
                viewBinding.pulseValue.text = profile.pulse ?: getString(R.string.profile_item_health_diary_empty)
                viewBinding.bloodGlucoseValue.text =
                    profile.bloodGlucose ?: getString(R.string.profile_item_health_diary_empty)
                viewBinding.heightValue.text = profile.height ?: getString(R.string.profile_item_health_diary_empty)
                viewBinding.weightValue.text = profile.weight ?: getString(R.string.profile_item_health_diary_empty)
            })
        }
    }

    private fun setProfileCard(profile: Profile) {
        viewBinding.profileName.text = profile.name
        viewBinding.nameEt.setText(profile.name)
        currentColor.value = profile.color

        if (profile.photo != DEFAULT_PROFILE_ICON) {
            viewBinding.edit.setBackgroundResource(R.drawable.green_border_bgr_alpha40)
            viewBinding.done.setBackgroundResource(R.drawable.green_border_bgr_alpha40)
            Glide.with(this)
                .load(profile.photo)
                .centerCrop()
                .into(viewBinding.photo)
        } else {
            viewBinding.edit.setBackgroundResource(R.drawable.green_border_bgr)
            viewBinding.done.setBackgroundResource(R.drawable.green_border_bgr)
            Glide.with(this)
                .load(R.drawable.ic_user_144dp)
                .centerInside()
                .into(viewBinding.photo)
        }
    }

    private fun pickImages(items: List<ImagePicker>) {

        adapterImagePickerDialog.items = items + DeleteImage(DEFAULT_ID_FOR_ENTITY)

        imagePickerDialog = MaterialDialog(requireActivity()).show {
            title(R.string.profile_change_photo)
            customListAdapter(adapterImagePickerDialog)
            val itemCount = getListAdapter()?.itemCount ?: 0
            getRecyclerView().addItemDecoration(
                MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_s), itemCount - 1)
            )
        }
    }

    private fun startColorDialog() {

        val colors = intArrayOf(
            getColor(requireActivity(), R.color.red_500),
            getColor(requireActivity(), R.color.pink_500),
            getColor(requireActivity(), R.color.deep_purple_500),
            getColor(requireActivity(), R.color.indigo_500),
            getColor(requireActivity(), R.color.light_blue_500),
            getColor(requireActivity(), R.color.cyan_500),
            getColor(requireActivity(), R.color.teal_500),
            getColor(requireActivity(), R.color.green_500),
            getColor(requireActivity(), R.color.lime_500),
            getColor(requireActivity(), R.color.yellow_500),
            getColor(requireActivity(), R.color.amber_500),
            getColor(requireActivity(), R.color.deep_orange_500)
        )
        val initialColor = args.profile?.color ?: 0

        MaterialDialog(requireActivity()).show {
            title(R.string.profile_choose_color)
            colorChooser(colors, initialSelection = initialColor) { _, color ->
                currentColor.value = color
            }
            negativeButton(R.string.cancel) { cancel() }
        }
    }

    private fun deleteProfile() {
        val name = viewBinding.profileName.text
        MaterialDialog(requireActivity()).show {
            if (name.isNotBlank()) {
                title(text = getString(R.string.profile_delete_explanation, name))
            } else {
                title(text = getString(R.string.profile_delete_explanation_empty))
            }
            positiveButton {
                if (args.profile?.id != null) {
                    viewModel.deleteProfile(args.profile?.id!!)
                } else {
                    viewModel.back()
                }
                cancel()
            }
            negativeButton { cancel() }
        }
    }

    private fun profileBuilder(): Profile? {
        return if (isNameValid() && isColorChosen()) {
            val id = args.profile?.id ?: DEFAULT_ID_FOR_ENTITY
            val etName = viewBinding.nameEt.text.toString()
            val name = if (etName.isNotBlank()) etName else args.profile?.name ?: ""
            val photoUri = currentPhotoUri ?: args.profile?.photo ?: DEFAULT_PROFILE_ICON
            Profile(id = id, name = name, color = requireNotNull(currentColor.value), photo = photoUri)
        } else {
            null
        }
    }

    private fun setEditMode(isVisible: Boolean) {
        viewBinding.currentProfile.isVisible = !isVisible
        viewBinding.editProfile.isVisible = isVisible
    }

    private fun isNameValid(): Boolean {
        val name = viewBinding.nameEt.text.toString()
        return if (name.isNotBlank()) true
        else {
            viewBinding.nameLayout.error = requireActivity().getString(R.string.error_empty_name)
            false
        }
    }

    private fun isColorChosen(): Boolean {
        return if (currentColor.value != null) true
        else {
            Toast.makeText(
                requireActivity(),
                resources.getString(R.string.error_profile_choose_color), Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = 0L
        private const val DEFAULT_PROFILE_ICON = "default_profile_icon"
    }
}
