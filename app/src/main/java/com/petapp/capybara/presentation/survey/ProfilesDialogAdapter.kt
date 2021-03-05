package com.petapp.capybara.presentation.survey

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Profile
import kotlinx.android.synthetic.main.item_profile_dialog.view.*

class ProfilesDialogAdapter(
    private val itemClick: (Profile) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(profilesDialogAdapterDelegate(itemClick))
        }
    }
}

fun profilesDialogAdapterDelegate(
    itemClick: (Profile) -> Unit
) = adapterDelegateLayoutContainer<Profile, ListItem>(R.layout.item_profile_dialog) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick.invoke(item) }
            title.text = item.name
            Glide.with(this)
                .load(item.photo)
                .error(R.drawable.ic_user_42dp)
                .centerCrop()
                .into(profile_icon)
        }
    }
}
