package com.petapp.capybara.presentation.surveys

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import kotlinx.android.synthetic.main.item_survey.view.*

class SurveysAdapter(
    private val itemClick: (Survey) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(surveysAdapterDelegate(itemClick))
        }
    }
}

fun surveysAdapterDelegate(
    itemClick: (Survey) -> Unit
) = adapterDelegateLayoutContainer<Survey, ListItem>(R.layout.item_profile) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick(item) }
            title.text = item.name
            date.text = item.date
        }
    }
}
