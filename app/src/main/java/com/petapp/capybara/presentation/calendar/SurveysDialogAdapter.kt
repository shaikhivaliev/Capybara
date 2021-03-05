package com.petapp.capybara.presentation.calendar

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.Date
import com.petapp.capybara.data.model.Survey
import kotlinx.android.synthetic.main.item_survey.view.title
import kotlinx.android.synthetic.main.item_survey_dialog.view.*

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
) = adapterDelegateLayoutContainer<Survey, ListItem>(R.layout.item_survey_dialog) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick(item) }
            title.text = item.name
            Glide.with(this)
                .load(item.profileIcon)
                .error(R.drawable.ic_user_42dp)
                .centerCrop()
                .into(profile_icon)
        }
    }
}

fun addNewSurveyAdapterDelegate(
    itemClick: (java.util.Date) -> Unit
) = adapterDelegateLayoutContainer<Date, ListItem>(R.layout.item_add_new_survey) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick(item.calendar) }
        }
    }
}
