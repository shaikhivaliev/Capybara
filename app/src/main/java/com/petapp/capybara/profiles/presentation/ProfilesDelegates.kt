
package com.petapp.capybara.profiles.presentation
import android.text.Editable
import android.text.TextWatcher
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.common.BaseItem
import com.petapp.capybara.R
import com.petapp.capybara.profiles.domain.ProfileColor
import com.petapp.capybara.profiles.domain.ProfileEdit
import com.petapp.capybara.profiles.domain.Profile
import kotlinx.android.synthetic.main.item_color.*
import kotlinx.android.synthetic.main.item_edit_profile.*
import kotlinx.android.synthetic.main.item_new_profile.*

fun profileDelegate(onEditProfile: (Profile) -> Unit) = adapterDelegateLayoutContainer<Profile, BaseItem>(R.layout.item_new_profile) {
    bind {
        mark_title.setBackgroundResource(item.color)
        edit_title.setText(item.title)
        if (!item.isShowEditItem) {
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

        edit_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {item.title = s.toString()}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        edit_profile.setOnClickListener {
            onEditProfile(item.apply {
                isShowEditItem = isShowEditItem.not()
                profileEdit.isShowColorsItem = false
            })
        }
    }
}

fun editProfileDelegate(onEditColor: (ProfileEdit) -> Unit, onDeleteProfile: (ProfileEdit) -> Unit) =
    adapterDelegateLayoutContainer<ProfileEdit, BaseItem>(R.layout.item_edit_profile) {
        bind {
            edit_mark.setOnClickListener {
                onEditColor(item.apply { isShowColorsItem = isShowColorsItem.not() })
            }
            delete_profile.setOnClickListener { onDeleteProfile(item) }
        }
    }

fun colorDelegate(onChooseColor: (Int, Int) -> Unit) = adapterDelegateLayoutContainer<ProfileColor, BaseItem>(R.layout.item_color) {
    bind {
        // сетим уже выбранный цвет
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