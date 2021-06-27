package com.petapp.capybara.presentation.surveys

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.databinding.ItemSurveyBinding

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
) = adapterDelegateViewBinding<Survey, ListItem, ItemSurveyBinding>(
    { layoutInflater, root -> ItemSurveyBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick(item) }
            title.text = item.name
            date.text = item.date
            Glide.with(context)
                .load(item.typeIcon)
                .centerCrop()
                .into(icon)
        }
    }
}
