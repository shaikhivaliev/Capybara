package com.petapp.capybara.ui.profiles

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.model.Color
import com.petapp.capybara.model.EditProfile
import com.petapp.capybara.model.Profile
import com.petapp.capybara.ui.common.RecyclerItems
import kotlinx.android.synthetic.main.item_color.*
import kotlinx.android.synthetic.main.item_edit_profile.*
import kotlinx.android.synthetic.main.item_new_profile.*

fun profileDelegate(onEditProfile: (Profile) -> Unit) = adapterDelegateLayoutContainer<Profile, RecyclerItems>(R.layout.item_new_profile) {
    bind {
        mark_title.setBackgroundResource(item.color)

        if (!item.isEditMode) {
            edit_title.isFocusable = false
            edit_title.isFocusableInTouchMode = false
            edit_title.isEnabled = false
            edit_profile.setImageResource(R.drawable.ic_edit)
        } else {
            edit_title.isFocusable = true
            edit_title.isFocusableInTouchMode = true
            edit_title.isEnabled = true
            edit_title.requestFocus()
            edit_profile.setImageResource(R.drawable.ic_save)
        }

        edit_profile.setOnClickListener {
            onEditProfile(item.apply {
                isExpandedEdit = isExpandedEdit.not()
                isEditMode = isEditMode.not()
                editProfile.isExpandedColor = false
            })
        }
    }
}

fun editProfileDelegate(onEditColor: (EditProfile) -> Unit, onDeleteProfile: (EditProfile) -> Unit) =
    adapterDelegateLayoutContainer<EditProfile, RecyclerItems>(R.layout.item_edit_profile) {
        bind {
            edit_mark.setOnClickListener {
                onEditColor(item.apply { isExpandedColor = isExpandedColor.not() })
            }
            delete_profile.setOnClickListener { onDeleteProfile(item) }
        }
    }

fun colorDelegate(onChooseColor: (Int, Int) -> Unit) = adapterDelegateLayoutContainer<Color, RecyclerItems>(R.layout.item_color) {
    bind {
        // сетим текущий выбранный цвет
        when (item.chosenColor) {
            android.R.color.white -> white.isChecked = true
            R.color.green -> green.isChecked = true
            R.color.red -> red.isChecked = true
            R.color.blue -> blue.isChecked = true
            R.color.yellow -> yellow.isChecked = true
            R.color.violet -> violet.isChecked = true
        }

        // listener на выбор меток
        color_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.white -> onChooseColor(item.parentId, android.R.color.white)
                R.id.green -> onChooseColor(item.parentId, R.color.green)
                R.id.red -> onChooseColor(item.parentId, R.color.red)
                R.id.blue -> onChooseColor(item.parentId, R.color.blue)
                R.id.yellow -> onChooseColor(item.parentId, R.color.yellow)
                R.id.violet -> onChooseColor(item.parentId, R.color.violet)
            }
        }
    }
}

