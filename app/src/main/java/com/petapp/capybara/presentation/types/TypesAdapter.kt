package com.petapp.capybara.presentation.types

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.core.list.ListItem
import com.petapp.capybara.core.list.ListItemDiffCallback
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.databinding.ItemTypeBinding

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
) = adapterDelegateViewBinding<Type, ListItem, ItemTypeBinding>(
    { layoutInflater, root -> ItemTypeBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick(item) }
            title.text = item.name
            surveysAmount.text = item.surveys.size.toString()
            Glide.with(context)
                .load(item.icon)
                .into(icon)
        }
    }
}
