package com.petapp.capybara.presentation.survey

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Type
import kotlinx.android.synthetic.main.item_type_dialog.view.*

class TypesDialogAdapter(
    private val itemClick: (Type) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback){
    init {
        with(delegatesManager) {
            addDelegate(typesDialogAdapterDelegate(itemClick))
        }
    }
}

fun typesDialogAdapterDelegate(
    itemClick: (Type) -> Unit
) = adapterDelegateLayoutContainer<Type, ListItem>(R.layout.item_type_dialog) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick.invoke(item) }
            title.text = item.name
            Glide.with(this)
                .load(item.icon)
                .fitCenter()
                .into(type_icon)
        }
    }
}
