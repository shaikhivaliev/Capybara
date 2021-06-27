package com.petapp.capybara.presentation.survey

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.databinding.ItemTypeDialogBinding

class TypesDialogAdapter(
    private val itemClick: (Type) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(typesDialogAdapterDelegate(itemClick))
        }
    }
}

fun typesDialogAdapterDelegate(
    itemClick: (Type) -> Unit
) = adapterDelegateViewBinding<Type, ListItem, ItemTypeDialogBinding>(
    { layoutInflater, root -> ItemTypeDialogBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick.invoke(item) }
            title.text = item.name
            Glide.with(context)
                .load(item.icon)
                .fitCenter()
                .into(typeIcon)
        }
    }
}
