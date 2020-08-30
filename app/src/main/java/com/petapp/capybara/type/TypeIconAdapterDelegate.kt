package com.petapp.capybara.type

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.petapp.capybara.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_type_icon.view.*

class TypeIconAdapterDelegate(
    private val iconClick: (Int) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_type_icon, parent, false)
        )
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Int
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as Int)
    }

    private inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        var iconRes: Int = 0

        init {
            containerView.setOnClickListener { iconClick(iconRes) }
        }

        fun bind(iconRes: Int) {
            this.iconRes = iconRes
            with(containerView) {
                Glide.with(this)
                    .load(iconRes)
                    .into(type_icon)
            }
        }
    }
}