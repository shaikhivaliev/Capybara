package com.petapp.profiles.presentation.profile

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
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.petapp.profiles.R
import com.petapp.profiles.argument
import com.petapp.profiles.createImageFile
import com.petapp.profiles.domain.Profile
import com.petapp.profiles.toast
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        private const val PROFILE_ID = "PROFILE_ID"
        private const val IS_NEW_PROFILE = "IS_NEW"

        fun create(country: String?, isNew: Boolean) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(PROFILE_ID, country)
                    putBoolean(IS_NEW_PROFILE, isNew)
                }
            }
    }

    private val profileId by argument(PROFILE_ID, "")
    private val isNewProfile by argument(IS_NEW_PROFILE, false)
    private var currentPhotoUri: Uri? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: ProfileViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.inject(this)
        activity?.let { viewModel = ViewModelProvider(it, factory).get(ProfileViewModel::class.java) }

        viewModel.getProfile(profileId)
        initObservers()

        name_et.doAfterTextChanged { name_layout.error = null }

        color_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.white -> color_mark.setBackgroundResource(android.R.color.white)
                R.id.green -> color_mark.setBackgroundResource(R.color.green)
                R.id.red -> color_mark.setBackgroundResource(R.color.red)
                R.id.blue -> color_mark.setBackgroundResource(R.color.blue)
                R.id.yellow -> color_mark.setBackgroundResource(R.color.yellow)
                R.id.violet -> color_mark.setBackgroundResource(R.color.violet)
            }
        }

        edit_photo.setOnClickListener { startForResult() }

        delete_profile.setOnClickListener { deleteItem() }

        done.setOnClickListener {
            if (isNameValid()) {
                val id = UniqueId.id.toString()
                val name = if (name_et.text.toString()
                        .isNotBlank()
                ) name_et.text.toString() else profile_name.text.toString()
                val color = getChipColor()
                val newProfile = Profile(id, name, color, currentPhotoUri.toString())
                val updateProfile = Profile(profileId, name, color, currentPhotoUri.toString())
                if (isNewProfile) viewModel.insertProfile(newProfile) else viewModel.updateProfile(
                    updateProfile
                )
            }
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
        val profileName = name_et.text.toString()
        return if (profileName.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    private fun initObservers() {
        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            if (profile == null) return@Observer
            setProfileCard(profile)
        })
        viewModel.isChangeDone.observe(viewLifecycleOwner, Observer { isDone ->
            if (isDone) viewModel.navigateBack()
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            this.activity.toast(it)
        })
    }

    private fun setProfileCard(profile: Profile) {
        Glide.with(this)
            .load(profile.photo)
            .placeholder(R.drawable.ic_add_photo_black)
            .into(photo)

        color_mark.setBackgroundResource(profile.color)

        profile_name.text = profile.name

        name_et.setText(profile.name)

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

    private fun deleteItem() {
        val profileName = profile_name.text
        activity?.let {
            MaterialDialog(it).show {
                if (profileName.isNotBlank()) {
                    title(text = getString(R.string.cancel_explanation, profileName))
                } else {
                    title(text = getString(R.string.cancel_explanation_empty))
                }
                positiveButton {
                    if (!isNewProfile) viewModel.deleteProfile() else viewModel.navigateBack()
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
                            .placeholder(R.drawable.ic_add_photo_black)
                            .into(photo)

                    }
                    // gallery
                    try {
                        val intent = result.data
                        val uri = intent?.data
                        if (intent != null && uri != null) {
                            currentPhotoUri = uri
                            Glide.with(this)
                                .load(intent.data)
                                .placeholder(R.drawable.ic_add_photo_black)
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

    class UniqueId {
        companion object {
            private val c = AtomicInteger(0)
            val id: Int
                get() = c.incrementAndGet()
        }
    }

}