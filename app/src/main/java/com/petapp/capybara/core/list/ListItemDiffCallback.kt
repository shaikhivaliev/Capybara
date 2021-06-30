package com.petapp.capybara.core.list

import androidx.recyclerview.widget.DiffUtil

object ListItemDiffCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
        oldItem.id == newItem.id
}
