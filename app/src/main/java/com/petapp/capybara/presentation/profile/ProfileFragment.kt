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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.bumptech.glide.Glide
import com.petapp.capybara.R
import com.petapp.capybara.common.MarginItemDecoration
import com.petapp.capybara.data.model.ImagePicker
import com.petapp.capybara.data.model.ImagePickerType
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.extensions.hideKeyboard
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfileFragment : Fragment(R.layout.fragment_profile) {

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
        }
    )

    private var imagePickerDialog: MaterialDialog? = null

    private val imageFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                Glide.with(this)
                    .load(currentPhotoUri)
                    .centerCrop()
                    .into(photo)
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
                .into(photo)
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
                .into(photo)
            name_et.requestFocus()
            name_et.showKeyboard()
        }
    }

    private fun initViews() {

        edit.setOnClickListener {
            setEditMode(true)
            delete_profile.isVisible = true
        }

        done.setOnClickListener {
            delete_profile.isVisible = false
            if (args.profile != null) {
                done.hideKeyboard()
                viewModel.updateProfile(profileBuilder())
            } else {
                done.hideKeyboard()
                viewModel.createProfile(profileBuilder())
            }
        }

        name_et.doAfterTextChanged { name_layout.error = null }

        change_photo.setOnClickListener { pickImages(listOf(camera, gallery)) }

        change_color.setOnClickListener { startColorDialog() }

        delete_profile.setOnClickListener { deleteProfile() }

        health_diary.setOnClickListener { viewModel.openHealthDiaryScreen(args.profile?.id) }
    }

    private fun initObservers() {
        with(viewModel) {
            profile.observe(viewLifecycleOwner, Observer { profile ->
                setProfileCard(profile)
                setEditMode(false)
            })

            imageFile.observe(viewLifecycleOwner, Observer { file ->
                currentPhotoUri = FileProvider.getUriForFile(
                    requireActivity(),
                    requireActivity().getString(R.string.autority),
                    file
                ).toString()
                imageFromCamera.launch(Uri.parse(currentPhotoUri))
            })

            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })

            currentColor.observe(viewLifecycleOwner, Observer { color ->
                color_marker.setBackgroundColor(color)
            })

            healthDiaryForProfile.observe(viewLifecycleOwner, Observer { profile ->
                blood_pressure_value.text = profile.bloodPressure ?: getString(R.string.profile_item_health_diary_empty)
                pulse_value.text = profile.pulse ?: getString(R.string.profile_item_health_diary_empty)
                blood_glucose_value.text = profile.bloodGlucose ?: getString(R.string.profile_item_health_diary_empty)
                height_value.text = profile.height ?: getString(R.string.profile_item_health_diary_empty)
                weight_value.text = profile.weight ?: getString(R.string.profile_item_health_diary_empty)
            })
        }
    }

    private fun setProfileCard(profile: Profile) {
        profile_name.text = profile.name
        name_et.setText(profile.name)
        currentColor.value = profile.color

        if (profile.photo != DEFAULT_PROFILE_ICON) {
            edit.setBackgroundResource(R.drawable.green_border_bgr_alpha40)
            done.setBackgroundResource(R.drawable.green_border_bgr_alpha40)
            Glide.with(this)
                .load(profile.photo)
                .centerCrop()
                .into(photo)
        } else {
            edit.setBackgroundResource(R.drawable.green_border_bgr)
            done.setBackgroundResource(R.drawable.green_border_bgr)
            Glide.with(this)
                .load(R.drawable.ic_user_144dp)
                .centerInside()
                .into(photo)
        }
    }

    private fun pickImages(items: List<ImagePicker>) {

        adapterImagePickerDialog.items = items

        imagePickerDialog = MaterialDialog(requireActivity()).show {
            title(R.string.profile_image_picker_dialog_title)
            customListAdapter(adapterImagePickerDialog)
            val itemCount = getListAdapter()?.itemCount ?: 0
            getRecyclerView().addItemDecoration(
                MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_ml), itemCount - 1)
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
        val name = profile_name.text
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
            val etName = name_et.text.toString()
            val name = if (etName.isNotBlank()) etName else args.profile?.name ?: ""
            val photoUri = currentPhotoUri ?: args.profile?.photo ?: DEFAULT_PROFILE_ICON
            Profile(id = id, name = name, color = requireNotNull(currentColor.value), photo = photoUri)
        } else {
            null
        }
    }

    private fun setEditMode(isVisible: Boolean) {
        current_profile.isVisible = !isVisible
        edit_profile.isVisible = isVisible
    }

    private fun isNameValid(): Boolean {
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = requireActivity().getString(R.string.error_empty_name)
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
