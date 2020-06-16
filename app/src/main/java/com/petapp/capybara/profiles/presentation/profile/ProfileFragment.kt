package com.petapp.capybara.profiles.presentation.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.petapp.capybara.R
import com.petapp.capybara.common.UniqueId
import com.petapp.capybara.extensions.argument
import com.petapp.capybara.extensions.createImageFile
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.profiles.domain.dto.Profile
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        private const val PROFILE_ID = "PROFILE_ID"
        private const val IS_NEW_PROFILE = "IS_NEW"
        private const val TRANSITION_NAME = "TRANSITION_NAME"

        fun createBundle(profileId: String?, isNew: Boolean, transitionName: String?): Bundle {
            return Bundle().apply {
                putString(PROFILE_ID, profileId)
                putBoolean(IS_NEW_PROFILE, isNew)
                putString(TRANSITION_NAME, transitionName)
            }
        }
    }

    private val profileId by argument(PROFILE_ID, "")
    private val isNewProfile by argument(IS_NEW_PROFILE, false)
    private val transitionName by argument(TRANSITION_NAME, "")
    private var currentPhotoUri: Uri? = null

    private val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        photo.transitionName = transitionName
        if (!isNewProfile) viewModel.getProfile(profileId)
        initObservers()
        done.showDone()

        name_et.doAfterTextChanged { name_layout.error = null }

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

        edit_photo.setOnClickListener { startForResult() }

        delete_profile.setOnClickListener { deleteProfile() }

        done.setOnClickListener {
            if (isNewProfile) createSurvey() else updateSurvey()
        }
    }

    private fun createSurvey() {
        if (isNameValid()) {
            val id = UniqueId.id.toString()
            val etName = name_et.text.toString()
            val name = if (etName.isNotBlank()) etName else profile_name.text.toString()
            val color = getChipColor()
            val newProfile = Profile(id, name, color, currentPhotoUri.toString())
            viewModel.createProfile(newProfile)
        }
    }

    private fun updateSurvey() {
        if (isNameValid()) {
            val etName = name_et.text.toString()
            val name = if (etName.isNotBlank()) etName else profile_name.text.toString()
            val color = getChipColor()
            val updateProfile = Profile(profileId, name, color, currentPhotoUri.toString())
            viewModel.updateProfile(updateProfile)
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
        if (!isNewProfile) return true
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    private fun initObservers() {
        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            setProfileCard(profile)
        })
        viewModel.isChangeDone.observe(viewLifecycleOwner, Observer { isDone ->
            if (isDone) navigateBack()
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            requireActivity().toast(it)
        })
    }

    private fun setProfileCard(profile: Profile) {
        profile_name.text = profile.name
        name_et.setText(profile.name)
        photo.setInitials(profile.name)
        profile.photo?.let {
            Glide.with(this)
                .load(profile.photo)
                .into(photo)
        }

        when (profile.color) {
            android.R.color.white -> white.isChecked = true
            R.color.green -> green.isChecked = true
            R.color.red -> red.isChecked = true
            R.color.blue -> blue.isChecked = true
            R.color.yellow -> yellow.isChecked = true
            R.color.violet -> violet.isChecked = true
        }
        currentPhotoUri = Uri.parse(profile.photo)
    }

    private fun deleteProfile() {
        val name = profile_name.text
        activity?.let {
            MaterialDialog(it).show {
                if (name.isNotBlank()) {
                    title(text = getString(R.string.cancel_explanation, name))
                } else {
                    title(text = getString(R.string.cancel_explanation_empty))
                }
                positiveButton {
                    if (!isNewProfile) viewModel.deleteProfile(profileId) else navigateBack()
                    cancel()
                }
                negativeButton { cancel() }
            }
        }
    }

    private fun startForResult() {
        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // camera
                    if (result.data == null) {
                        Glide.with(this)
                            .load(currentPhotoUri)
                            .into(photo)
                    }
                    // gallery
                    try {
                        val intent = result.data
                        val uri = intent?.data
                        if (intent != null && uri != null) {
                            currentPhotoUri = uri
                            Glide.with(this)
                                .load(currentPhotoUri)
                                .into(photo)

                            val takeFlags =
                                intent.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                            activity?.contentResolver?.takePersistableUriPermission(uri, takeFlags)
                        }
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                }
            }

        val intentArray: Array<Intent?> = arrayOf(openCamera())
        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_INTENT, openGallery())
        chooser.putExtra(Intent.EXTRA_TITLE, "Выберите изображение")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

        startForResult.launch(chooser)
    }

    private fun openCamera(): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.let {
            if (cameraIntent.resolveActivity(it.packageManager) != null) {
                val photoFile = it.createImageFile()
                currentPhotoUri = FileProvider.getUriForFile(it, "com.petapp.capybara", photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            }
        }
        return cameraIntent
    }

    private fun openGallery(): Intent {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        galleryIntent.action = Intent.ACTION_OPEN_DOCUMENT
        return galleryIntent
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.tab_profiles)
    }
}