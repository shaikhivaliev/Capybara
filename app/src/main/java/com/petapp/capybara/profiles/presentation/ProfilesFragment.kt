package com.petapp.capybara.profiles.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.common.ItemsAdapter
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.profiles.domain.ProfileEdit
import com.petapp.capybara.profiles.domain.Profile
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val viewModel: ProfilesViewModel by viewModel()

    private val adapter: ItemsAdapter by lazy {
        ItemsAdapter(
            profileDelegate { viewModel.setProfileState(it) },
            editProfileDelegate(
                onEditColor = { viewModel.showColorsItem() },
                onDeleteProfile = { deleteItem(it) }
            ),
            colorDelegate { parentId, color -> viewModel.setColor(parentId, color) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateProfiles()
        initObservers()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
        }
        add_profile.setOnClickListener {
            viewModel.insertProfile(Profile())
        }
    }

    private fun initObservers() {
        viewModel.profiles.observe(this, Observer {
            adapter.items = viewModel.flatten(it)
            recycler_view.post { adapter.notifyDataSetChanged() }
        })
        viewModel.isShowMock.observe(this, Observer { isShow ->
            mock.visible(isShow)
        })
    }

    private fun deleteItem(profileEdit: ProfileEdit) {
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
    }
}