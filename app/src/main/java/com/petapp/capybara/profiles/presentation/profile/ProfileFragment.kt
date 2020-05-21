package com.petapp.capybara.profiles.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.petapp.capybara.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        private const val PROFILE_ID = "PROFILE_ID"

        fun create(country: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(PROFILE_ID, country)
                }
            }
    }

    private val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun deleteItem() {
/*
        activity?.let {
            MaterialDialog(it).show {
                title(text = getString(R.string.cancel_explanation, profileEdit.parentTitle))
                positiveButton {
                    viewModel.deleteProfile(profileEdit.parentId)
                    cancel()
                }
                negativeButton { cancel() }
            }
        }
*/
    }

/*
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
*/

/*
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
*/


}