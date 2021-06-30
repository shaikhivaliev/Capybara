package com.petapp.capybara.presentation.profiles

import android.content.res.ColorStateList
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.R
import com.petapp.capybara.core.list.ListItem
import com.petapp.capybara.core.list.ListItemDiffCallback
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.databinding.ItemProfileBinding

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
) = adapterDelegateViewBinding<Profile, ListItem, ItemProfileBinding>(
    { layoutInflater, root -> ItemProfileBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick.invoke(item) }
            profileName.text = item.name
            photo.strokeColor = ColorStateList.valueOf(item.color)
            photo.transitionName = item.name
            surveysAmount.text = item.surveys.size.toString()
            Glide.with(context)
                .load(item.photo)
                .error(R.drawable.ic_user_42dp)
                .centerCrop()
                .into(photo)
        }
    }
}
