package com.petapp.capybara.presentation.healthDiary

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.R
import com.petapp.capybara.core.list.ListItem
import com.petapp.capybara.core.list.ListItemDiffCallback
import com.petapp.capybara.data.model.healthDiary.EmptyItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.databinding.ItemEmptySurveyBinding
import com.petapp.capybara.databinding.ItemStepHealthDiaryBinding
import com.petapp.capybara.databinding.ItemSurveyHealthDiaryBinding

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
    adapterDelegateViewBinding<ItemHealthDiary, ListItem, ItemStepHealthDiaryBinding>(
        { layoutInflater, root -> ItemStepHealthDiaryBinding.inflate(layoutInflater, root, false) }
    ) {

        fun showAddingButton(isExpanded: Boolean) {
            with(binding) {
                addHealthDiarySurvey.isVisible = item.isExpanded

                val rotationAngle = if (isExpanded) ROTATION_ANGLE else 0
                arrowDown.animate()
                    .rotation(rotationAngle.toFloat())
                    .setDuration(ROTATION_DURATION)
                    .start()
            }
        }

        bind {
            showAddingButton(item.isExpanded)
            with(binding) {
                title.text = getString(
                    when (item.type) {
                        HealthDiaryType.BLOOD_PRESSURE -> R.string.health_diary_blood_pressure_title
                        HealthDiaryType.BLOOD_GLUCOSE -> R.string.health_diary_blood_glucose_title
                        HealthDiaryType.PULSE -> R.string.health_diary_pulse_title
                        HealthDiaryType.HEIGHT -> R.string.health_diary_height_title
                        HealthDiaryType.WEIGHT -> R.string.health_diary_weight_title
                    }
                )
                arrowDown.setOnClickListener {
                    expandItem(item)
                    showAddingButton(!item.isExpanded)
                }
                addHealthDiarySurvey.setOnClickListener {
                    addSurvey(item)
                }
            }
        }
    }

fun emptySurveyHealthDiaryDelegate() =
    adapterDelegateViewBinding<EmptyItemHealthDiary, ListItem, ItemEmptySurveyBinding>(
        { layoutInflater, root -> ItemEmptySurveyBinding.inflate(layoutInflater, root, false) }
    ) {
        bind { }
    }

fun surveyHealthDiaryDelegate(onDelete: (SurveyHealthDiary) -> Unit) =
    adapterDelegateViewBinding<SurveyHealthDiary, ListItem, ItemSurveyHealthDiaryBinding>(
        { layoutInflater, root -> ItemSurveyHealthDiaryBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            with(binding) {
                delete.setOnClickListener { onDelete(item) }
                date.text = item.date
                time.text = item.time
                surveyValue.text = item.surveyValue
                unitOfMeasure.text = item.unitOfMeasure
            }
        }
    }
