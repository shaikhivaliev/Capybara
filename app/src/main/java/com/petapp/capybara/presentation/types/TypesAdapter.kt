package com.petapp.capybara.presentation.types

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Type
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_type.view.*

class TypesAdapter(
    private val itemClick: (Type) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(TypesAdapterDelegate(itemClick))
        }
    }
}

class TypesAdapterDelegate(
    private val itemClick: (Type) -> Unit
) : AdapterDelegate<MutableList<ListItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        )
    }

    override fun isForViewType(items: MutableList<ListItem>, position: Int): Boolean {
        return items[position] is Type
    }

    override fun onBindViewHolder(
        items: MutableList<ListItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as Type)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        lateinit var type: Type

        init {
            containerView.setOnClickListener { itemClick(type) }
        }

        fun bind(type: Type) {
            this.type = type
            with(containerView) {
                title.text = type.name
                surveys_amount.text = type.surveys.size.toString()
                Glide.with(this)
                    .load(type.icon)
                    .into(icon)
            }
        }
    }
}
