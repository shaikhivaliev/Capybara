package com.petapp.capybara.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Settings
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_settings.view.*

class SettingsAdapterDelegate(
    private val itemClick: (Settings) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_settings, parent, false))
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Settings
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as Settings)
    }

    private inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var settings: Settings

        init {
            containerView.setOnClickListener { itemClick(settings) }
        }

        fun bind(settings: Settings) {
            this.settings = settings
            with(containerView) {
                text.text = context.getString(settings.name)
                image.setImageResource(settings.image)
            }
        }
    }
}
