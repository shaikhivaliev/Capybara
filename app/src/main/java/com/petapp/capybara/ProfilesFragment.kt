package com.petapp.capybara

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.common.ItemsAdapter
import kotlinx.android.synthetic.main.fragment_profiles.*

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val profiles: MutableList<Profile> = arrayListOf()

    private val adapter: ItemsAdapter by lazy {
        ItemsAdapter(
            profileDelegate { setExtendableEdit(it) },
            editProfileDelegate(
                onEditColor = { setExtendableColor(it) },
                onDeleteProfile = { deleteItem(it) }
            ),
            colorDelegate { parentId, color -> setColor(parentId, color) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
        }
        fab.setOnClickListener {
            profiles.add(Profile(title = "Title", isExpandedEdit = true, isEditMode = true))
            showMock()
            submitItems()
            recycler_view.smoothScrollToPosition(adapter.itemCount)
        }
        showMock()
    }

    private fun setExtendableEdit(profile: Profile) {
        profiles.filter { it.id != profile.id }.forEach {
            // убираем другие развернутные items
            it.isExpandedEdit = false
            it.isEditMode = false
            it.editProfile.isExpandedColor = false
        }
        adapter.notifyDataSetChanged()
        submitItems()
    }

    private fun setExtendableColor(editProfile: EditProfile) {
        // отмечаем цвет профиля в chips
        val profileColor = profiles.find { it.id == editProfile.parentId }?.color
        profileColor?.let { editProfile.color.chosenColor = profileColor }
        submitItems()
    }

    private fun deleteItem(editProfile: EditProfile) {
        val profile = profiles.find { it.id == editProfile.parentId }
        activity?.let {
            MaterialDialog(it).show {
                title(text = getString(R.string.cancel_explanation, profile?.title))
                positiveButton {
                    profiles.remove(profile)
                    showMock()
                    submitItems()
                    cancel()
                }
                negativeButton { cancel() }
            }
        }

    }

    private fun setColor(parentId: Int, color: Int) {
        val profile = profiles.find { it.id == parentId }
        profile?.let {
            it.editProfile.color.chosenColor = color
            it.color = color
            recycler_view.post { adapter.notifyDataSetChanged() }
            submitItems()
        }
    }

    private fun submitItems() {
        adapter.items = profiles.flatten()
    }

    private fun showMock() {
        mock.visible(profiles.size == 0)
        recycler_view.visible(profiles.size != 0)
    }

}