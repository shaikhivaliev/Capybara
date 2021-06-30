package com.petapp.capybara.presentation.calendar

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.R
import com.petapp.capybara.core.list.ListItem
import com.petapp.capybara.core.list.ListItemDiffCallback
import com.petapp.capybara.data.model.DateModel
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.databinding.ItemAddNewSurveyBinding
import com.petapp.capybara.databinding.ItemSurveyDialogBinding

class SurveysDialogAdapter(
    private val itemClick: (Survey) -> Unit,
    private val addNewSurvey: (java.util.Date) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(surveysAdapterDelegate(itemClick))
            addDelegate(addNewSurveyAdapterDelegate(addNewSurvey))
        }
    }
}

fun surveysAdapterDelegate(
    itemClick: (Survey) -> Unit
) = adapterDelegateViewBinding<Survey, ListItem, ItemSurveyDialogBinding>(
    { layoutInflater, root -> ItemSurveyDialogBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick(item) }
            title.text = item.name
            Glide.with(context)
                .load(item.profileIcon)
                .error(R.drawable.ic_user_42dp)
                .centerCrop()
                .into(profileIcon)
        }
    }
}

fun addNewSurveyAdapterDelegate(
    itemClick: (java.util.Date) -> Unit
) = adapterDelegateViewBinding<DateModel, ListItem, ItemAddNewSurveyBinding>(
    { layoutInflater, root -> ItemAddNewSurveyBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick(item.calendar) }
        }
    }
}
