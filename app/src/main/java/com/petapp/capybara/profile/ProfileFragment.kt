package com.petapp.capybara.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
import com.petapp.capybara.extensions.createImageFile
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: ProfileFragmentArgs by navArgs()

    private var currentPhotoUri: String = ""

    private val pickImagesResult =
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
                        currentPhotoUri = uri.toString()
                        Glide.with(this)
                            .load(uri)
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
            val id = args.profile?.id ?: ""
            val etName = name_et.text.toString()
            val name = if (etName.isNotBlank()) etName else args.profile?.name ?: ""
            val color = getChipColor()
            val photoUri = if (currentPhotoUri.isNotEmpty()) currentPhotoUri else args.profile?.photo ?: ""
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
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setProfileCard(profile: Profile) {
        profile_name.text = profile.name
        name_et.setText(profile.name)
        photo.setInitials(profile.name)
        if (profile.photo.isNotEmpty()) {
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
        currentPhotoUri = profile.photo
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
        val intentArray: Array<Intent?> = arrayOf(openCamera())
        val chooser = Intent(Intent.ACTION_CHOOSER)
        chooser.putExtra(Intent.EXTRA_INTENT, openGallery())
        chooser.putExtra(Intent.EXTRA_TITLE, "Выберите изображение")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)

        pickImagesResult.launch(chooser)
    }

    private fun openCamera(): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.let {
            if (cameraIntent.resolveActivity(it.packageManager) != null) {
                val photoFile = it.createImageFile()
                currentPhotoUri = FileProvider.getUriForFile(it, "com.petapp.capybara", photoFile).toString()
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
}