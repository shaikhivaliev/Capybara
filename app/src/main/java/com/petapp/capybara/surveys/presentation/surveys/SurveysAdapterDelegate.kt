package com.petapp.capybara.surveys.presentation.surveys

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.petapp.capybara.R
import com.petapp.capybara.surveys.domain.dto.Survey
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_survey.view.*

class SurveysAdapterDelegate(
    private val itemClick: (Survey) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_survey, parent, false)
        )
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Survey
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as ViewHolder).bind(items[position] as Survey)
    }

    private inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private lateinit var survey: Survey

        init {
            containerView.setOnClickListener { itemClick(survey) }
        }

        fun bind(survey: Survey) {
            this.survey = survey
            containerView.survey_name.text = survey.name
        }
    }
}