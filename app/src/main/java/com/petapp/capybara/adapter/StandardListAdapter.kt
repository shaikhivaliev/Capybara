package com.petapp.capybara.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class StandardListAdapter<T : ListDiffer<T>>(vararg delegates: AdapterDelegate<List<T>>) :
    AsyncListDifferDelegationAdapter<T>(
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.areItemsTheSame(newItem)
            override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.areContentsTheSame(newItem)
            override fun getChangePayload(oldItem: T, newItem: T) = Any()
        }
    ) {
    init {
        delegates.forEach { delegatesManager.addDelegate(it) }
    }
}
