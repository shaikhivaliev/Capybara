package com.petapp.capybara.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
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

    private val imageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            Glide.with(this)
                .load(currentPhotoUri)
                .into(photo)
        } else {
            currentPhotoUri = args.profile?.photo
        }
    }

    private val imageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.apply {
            currentPhotoUri = this.toString()
            Glide.with(requireActivity())
                .load(this)
                .into(photo)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        done.showDone()
        args.profile?.id?.apply { viewModel.getProfile(this) }

        color_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.white -> photo.setColor(android.R.color.white)
                R.id.green -> photo.setColor(R.color.green)
                R.id.red -> photo.setColor(R.color.red)
                R.id.blue -> photo.setColor(R.color.blue)
                R.id.yellow -> photo.setColor(R.color.yellow)
                R.id.violet -> photo.setColor(R.color.violet)
            }
        }

        edit_photo.setOnClickListener { pickImages() }

        delete_profile.setOnClickListener { deleteProfile() }

        name_et.doAfterTextChanged { name_layout.error = null }

        done.setOnClickListener {
            if (args.profile != null) {
                viewModel.updateProfile(profileFactory())
            } else {
                viewModel.createProfile(profileFactory())
            }
        }
    }

    private fun profileFactory(): Profile? {
        return if (isNameValid()) {
            val id = args.profile?.id ?: DEFAULT_ID_FOR_ENTITY
            val etName = name_et.text.toString()
            val name = if (etName.isNotBlank()) etName else args.profile?.name ?: ""
            val color = getChipColor()
            val photoUri = if (!currentPhotoUri.isNullOrEmpty()) currentPhotoUri else args.profile?.photo
            Profile(id = id, name = name, color = color, photo = photoUri)
        } else {
            null
        }
    }

    private fun getChipColor(): Int {
        return when (color_group.checkedChipId) {
            R.id.green -> R.color.green
            R.id.red -> R.color.red
            R.id.blue -> R.color.blue
            R.id.yellow -> R.color.yellow
            R.id.violet -> R.color.violet
            else -> android.R.color.white
        }
    }

    private fun isNameValid(): Boolean {
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    private fun initObservers() {
        with(viewModel) {
            profile.observe(viewLifecycleOwner, Observer { profile ->
                setProfileCard(profile)
            })

            imageFile.observe(viewLifecycleOwner, Observer { file ->
                currentPhotoUri = FileProvider.getUriForFile(requireActivity(), "com.petapp.capybara", file).toString()
                imageFromCamera.launch(Uri.parse(currentPhotoUri))
            })

            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setProfileCard(profile: Profile) {
        profile_name.text = profile.name
        name_et.setText(profile.name)
        if (!profile.photo.isNullOrEmpty()) {
            Glide.with(this)
                .load(profile.photo)
                .into(photo)
        } else {
            photo.setInitials(profile.name)
        }

        when (profile.color) {
            android.R.color.white -> white.isChecked = true
            R.color.green -> green.isChecked = true
            R.color.red -> red.isChecked = true
            R.color.blue -> blue.isChecked = true
            R.color.yellow -> yellow.isChecked = true
            R.color.violet -> violet.isChecked = true
        }
    }

    private fun deleteProfile() {
        val name = profile_name.text
        activity?.let {
            MaterialDialog(it).show {
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
    }

    private fun pickImages() {
        viewModel.createImageFile(requireActivity())
        //imageFromGallery.launch("image/*")
    }

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = "0"
    }
}