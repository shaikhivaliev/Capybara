package com.petapp.capybara.presentation.survey

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Mark
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_profile_dialog.view.*

class MarksDialogAdapterDelegate(
    private val itemClick: (Mark) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile_dialog, parent, false)
        )
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Mark
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as Mark)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        lateinit var mark: Mark

        init {
            containerView.setOnClickListener { itemClick(mark) }
        }

        fun bind(mark: Mark) {
            this.mark = mark
            with(containerView) {
                title.text = mark.name
                mark_bgr.setBackgroundColor(mark.color)
            }
        }
    }
}
