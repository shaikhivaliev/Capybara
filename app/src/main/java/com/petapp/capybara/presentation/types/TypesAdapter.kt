package com.petapp.capybara.presentation.types

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Type
import kotlinx.android.synthetic.main.item_type.view.*

class TypesAdapter(
    private val itemClick: (Type) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(typesAdapterDelegate(itemClick))
        }
    }
}

fun typesAdapterDelegate(
    itemClick: (Type) -> Unit
) = adapterDelegateLayoutContainer<Type, ListItem>(R.layout.item_type) {

    bind {
        with(containerView) {
            setOnClickListener { itemClick(item) }
            title.text = item.name
            surveys_amount.text = item.surveys.size.toString()
            Glide.with(this)
                .load(item.icon)
                .into(icon)
        }
    }
}
