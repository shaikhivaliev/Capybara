package com.petapp.capybara.ui.common

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ItemsAdapter(vararg delegates: AdapterDelegate<List<RecyclerItems>>) :
    AsyncListDifferDelegationAdapter<RecyclerItems>(
        object : DiffUtil.ItemCallback<RecyclerItems>() {
            override fun areItemsTheSame(oldItem: RecyclerItems, newItem: RecyclerItems) = oldItem.isSame(newItem)
            override fun areContentsTheSame(oldItem: RecyclerItems, newItem: RecyclerItems) = oldItem == newItem
            override fun getChangePayload(oldItem: RecyclerItems, newItem: RecyclerItems) = Any()
        }
    ) {

    init {
        delegates.forEach { delegatesManager.addDelegate(it) }
    }
}