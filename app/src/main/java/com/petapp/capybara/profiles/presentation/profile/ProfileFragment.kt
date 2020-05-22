package com.petapp.capybara.profiles.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.petapp.capybara.R
import com.petapp.capybara.extensions.argument
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.profiles.domain.Profile
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.atomic.AtomicInteger

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

    private val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfile(profileId)
        initObservers()

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

        edit_photo.setOnClickListener { }

        delete_profile.setOnClickListener { deleteItem() }

        done.setOnClickListener {
            if (isNameValid()) {
                val id = UniqueId.id.toString()
                val name = if (name_et.text.toString().isNotBlank()) name_et.text.toString() else profile_name.text.toString()
                val color = android.R.color.black
                val newProfile = Profile(id, name, color, null)
                val updateProfile = Profile(profileId, name, color, null)
                if (isNewProfile) viewModel.insertProfile(newProfile) else viewModel.updateProfile(updateProfile)
            }
        }
    }

    private fun isNameValid(): Boolean {
        if (!isNewProfile) return true
        val profileName = name_et.text
        return if (profileName != null) true
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

        name_et.setText(profile.name)

        color_mark.setBackgroundResource(profile.color)

        when (profile.color) {
            android.R.color.white -> white.isChecked = true
            R.color.green -> green.isChecked = true
            R.color.red -> red.isChecked = true
            R.color.blue -> blue.isChecked = true
            R.color.yellow -> yellow.isChecked = true
            R.color.violet -> violet.isChecked = true
        }

        profile_name.text = profile.name

        Glide.with(this)
            .load(profile.photo)
            .placeholder(R.drawable.ic_add_photo_black)
            .into(photo)
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

    class UniqueId {
        companion object {
            private val c = AtomicInteger(0)
            val id: Int
                get() = c.incrementAndGet()
        }
    }


}