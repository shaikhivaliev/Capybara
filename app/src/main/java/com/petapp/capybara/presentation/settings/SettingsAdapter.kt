package com.petapp.capybara.presentation.settings

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Settings
import com.petapp.capybara.databinding.ItemSettingsBinding

class SettingsAdapter(
    private val itemClick: (Settings) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(settingsAdapterDelegate(itemClick))
        }
    }
}

fun settingsAdapterDelegate(
    itemClick: (Settings) -> Unit
) = adapterDelegateViewBinding<Settings, ListItem, ItemSettingsBinding>(
    { layoutInflater, root -> ItemSettingsBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick(item) }
            text.text = context.getString(item.name)
            image.setImageResource(item.image)
        }
    }
}
