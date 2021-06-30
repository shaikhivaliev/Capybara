package com.petapp.capybara.presentation.survey

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.R
import com.petapp.capybara.core.list.ListItem
import com.petapp.capybara.core.list.ListItemDiffCallback
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.databinding.ItemProfileDialogBinding

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
) = adapterDelegateViewBinding<Profile, ListItem, ItemProfileDialogBinding>(
    { layoutInflater, root -> ItemProfileDialogBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick.invoke(item) }
            title.text = item.name
            Glide.with(context)
                .load(item.photo)
                .error(R.drawable.ic_user_42dp)
                .centerCrop()
                .into(profileIcon)
        }
    }
}
