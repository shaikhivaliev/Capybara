package com.petapp.capybara.surveys.presentation.types

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.petapp.capybara.R
import com.petapp.capybara.surveys.domain.dto.Type
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_type.view.*

class TypesAdapterDelegate(
    private val itemClick: (Type) -> Unit,
    private val editClick: (Type) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        )
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Type
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as Type)
    }

    private inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var type: Type

        init {
            containerView.setOnClickListener { itemClick(type) }
            containerView.edit.setOnClickListener { editClick(type) }
        }

        fun bind(type: Type) {
            this.type = type
            containerView.title.text = type.name
            containerView.surveys_amount.text = type.amount
            Glide.with(containerView)
                .load(type.icon)
                .placeholder(R.drawable.ic_add_photo_black)
                .into(containerView.icon)

        }
    }
}