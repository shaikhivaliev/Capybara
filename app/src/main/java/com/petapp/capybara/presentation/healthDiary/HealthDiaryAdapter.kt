package com.petapp.capybara.presentation.healthDiary

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.healthDiary.EmptyItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import kotlinx.android.synthetic.main.item_step_health_diary.view.*
import kotlinx.android.synthetic.main.item_survey_health_diary.view.*

private const val ROTATION_ANGLE = 180L
private const val ROTATION_DURATION = 400L

class HealthDiaryAdapter(
    expandItem: (ItemHealthDiary) -> Unit,
    addSurvey: (ItemHealthDiary) -> Unit,
    onDelete: (SurveyHealthDiary) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {

    init {
        setHasStableIds(true)
        with(delegatesManager) {
            addDelegate(itemHealthDiaryDelegate(expandItem, addSurvey))
            addDelegate(emptySurveyHealthDiaryDelegate())
            addDelegate(surveyHealthDiaryDelegate(onDelete))
        }
    }

    override fun getItemId(position: Int): Long = items[position].id
}

fun itemHealthDiaryDelegate(
    expandItem: (ItemHealthDiary) -> Unit,
    addSurvey: (ItemHealthDiary) -> Unit
) =
    adapterDelegateLayoutContainer<ItemHealthDiary, ListItem>(R.layout.item_step_health_diary) {

        fun showAddingButton(isExpanded: Boolean) {
            with(itemView) {
                add_health_diary_survey.isVisible = item.isExpanded

                val rotationAngle = if (isExpanded) ROTATION_ANGLE else 0
                arrow_down.animate()
                    .rotation(rotationAngle.toFloat())
                    .setDuration(ROTATION_DURATION)
                    .start()
            }
        }

        bind {
            showAddingButton(item.isExpanded)
            with(itemView) {
                title.text = getString(
                    when (item.type) {
                        HealthDiaryType.BLOOD_PRESSURE -> R.string.health_diary_blood_pressure_title
                        HealthDiaryType.BLOOD_GLUCOSE -> R.string.health_diary_blood_glucose_title
                        HealthDiaryType.PULSE -> R.string.health_diary_pulse_title
                        HealthDiaryType.HEIGHT -> R.string.health_diary_height_title
                        HealthDiaryType.WEIGHT -> R.string.health_diary_weight_title
                    }
                )
                arrow_down.setOnClickListener {
                    expandItem(item.apply {
                        isExpanded = isExpanded.not()
                        showAddingButton(isExpanded)
                    })
                }
                add_health_diary_survey.setOnClickListener {
                    addSurvey(item)
                }
            }
        }
    }

fun emptySurveyHealthDiaryDelegate() =
    adapterDelegateLayoutContainer<EmptyItemHealthDiary, ListItem>(R.layout.item_empty_survey) {
        bind { }
    }

fun surveyHealthDiaryDelegate(onDelete: (SurveyHealthDiary) -> Unit) =
    adapterDelegateLayoutContainer<SurveyHealthDiary, ListItem>(R.layout.item_survey_health_diary) {
        itemView.delete.setOnClickListener { onDelete(item) }
        bind {
            with(itemView) {
                date.text = item.date
                time.text = item.time
                survey_value.text = item.surveyValue
                unit_of_measure.text = item.unitOfMeasure
            }
        }
    }
