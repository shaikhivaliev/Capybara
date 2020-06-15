package com.petapp.capybara.common

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ItemsAdapter(vararg delegates: AdapterDelegate<List<BaseItem>>) :
    AsyncListDifferDelegationAdapter<BaseItem>(
        object : DiffUtil.ItemCallback<BaseItem>() {
            override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem) = oldItem.isSame(newItem)
            override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem) = oldItem == newItem
            override fun getChangePayload(oldItem: BaseItem, newItem: BaseItem) = Any()
        }
    ) {

    init {
        delegates.forEach { delegatesManager.addDelegate(it) }
    }
}