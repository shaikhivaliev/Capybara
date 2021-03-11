package com.petapp.capybara.presentation.settings

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Settings
import kotlinx.android.synthetic.main.item_settings.view.*

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
) = adapterDelegateLayoutContainer<Settings, ListItem>(R.layout.item_settings) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick(item) }
            text.text = context.getString(item.name)
            image.setImageResource(item.image)
        }
    }
}
