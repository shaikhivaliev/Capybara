package com.petapp.capybara.presentation.profiles

import android.content.res.ColorStateList
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Profile
import kotlinx.android.synthetic.main.item_profile.view.*

class ProfilesAdapter(
    private val itemClick: (Profile) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(profilesAdapterDelegate(itemClick))
        }
    }
}

fun profilesAdapterDelegate(
    itemClick: (Profile) -> Unit
) = adapterDelegateLayoutContainer<Profile, ListItem>(R.layout.item_profile) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick.invoke(item) }
            profile_name.text = item.name
            photo.strokeColor = ColorStateList.valueOf(item.color)
            photo.transitionName = item.name
            surveys_amount.text = item.surveys.size.toString()
            Glide.with(this)
                .load(item.photo)
                .error(R.drawable.ic_user_42)
                .centerCrop()
                .into(photo)
        }
    }
}
