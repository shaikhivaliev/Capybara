package com.petapp.capybara.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.dialog_choose_pick_image.view.*
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

    private val imageFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                initials.visible(false)
                Glide.with(this)
                    .load(currentPhotoUri)
                    .centerCrop()
                    .into(photo)
            } else {
                currentPhotoUri = args.profile?.photo
            }
        }

    private val imageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.apply {
            currentPhotoUri = this.toString()
            initials.visible(false)
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

        args.profile?.id?.apply { viewModel.getProfile(this) }
        if (args.profile?.id == null) {
            current_profile.visible(false)
            edit_profile.visible(true)
            Glide.with(this)
                .load(R.drawable.ic_photo_mock)
                .into(photo)
            name_et.requestFocus()
            name_et.showKeyboard()
        }
    }

    private fun initViews() {

        edit.setOnClickListener {
            current_profile.visible(false)
            edit_profile.visible(true)
            name_et.setText(args.profile?.name)
        }

        done.setOnClickListener {
            if (args.profile != null) {
                viewModel.updateProfile(profileFactory())
            } else {
                viewModel.createProfile(profileFactory())
            }
        }

        name_et.doAfterTextChanged { name_layout.error = null }

        change_photo.setOnClickListener { pickImages() }

        change_color.setOnClickListener { startColorDialog() }

        delete_profile.setOnClickListener { deleteProfile() }
    }

    private fun initObservers() {
        with(viewModel) {
            profile.observe(viewLifecycleOwner, Observer { profile ->
                setProfileCard(profile)
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
        }
    }

    private fun setProfileCard(profile: Profile) {
        profile_name.text = profile.name
        currentColor.value = profile.color
        initials.visible(profile.photo.isNullOrEmpty())

        if (!profile.photo.isNullOrEmpty()) {
            edit.setBackgroundResource(R.drawable.green_border_bgr_alpha40)
            done.setBackgroundResource(R.drawable.green_border_bgr_alpha40)
            Glide.with(this)
                .load(profile.photo)
                .centerCrop()
                .into(photo)
        } else {
            edit.setBackgroundResource(R.drawable.green_border_bgr)
            done.setBackgroundResource(R.drawable.green_border_bgr)
            initials.text = profile.name.first().toString()
        }
    }

    private fun pickImages() {
        val bottomSheet = layoutInflater.inflate(R.layout.dialog_choose_pick_image, null)
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(bottomSheet)
        bottomSheet.add_photo_from_camera.setOnClickListener {
            viewModel.createImageFile(requireActivity())
            dialog.dismiss()
        }
        bottomSheet.add_photo_from_gallery.setOnClickListener {
            imageFromGallery.launch("image/*")
            dialog.dismiss()
        }
        dialog.show()
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

    private fun profileFactory(): Profile? {
        return if (isNameValid() && isColorChosen()) {
            val id = args.profile?.id ?: DEFAULT_ID_FOR_ENTITY
            val etName = name_et.text.toString()
            val name = if (etName.isNotBlank()) etName else args.profile?.name ?: ""
            val photoUri = if (!currentPhotoUri.isNullOrEmpty()) currentPhotoUri else args.profile?.photo
            Profile(id = id, name = name, color = requireNotNull(currentColor.value), photo = photoUri)
        } else {
            null
        }
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
            Toast.makeText(requireActivity(), "Выберете хотя бы один цвет", Toast.LENGTH_LONG).show()
            false
        }
    }

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = "0"
    }
}
