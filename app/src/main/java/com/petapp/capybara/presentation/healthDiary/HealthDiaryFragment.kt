package com.petapp.capybara.presentation.healthDiary

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.core.navigation.LongNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.databinding.DialogHealthDiarySurveyBinding
import com.petapp.capybara.databinding.FragmentHealthDiaryBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.surveys.SurveysFragment
import com.petapp.capybara.presentation.toPresentationModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HealthDiaryFragment : Fragment(R.layout.fragment_health_diary) {

    private val viewBinding by viewBinding(FragmentHealthDiaryBinding::bind)

    @Inject
    lateinit var vmFactory: HealthDiaryVmFactory

    private val vm: HealthDiaryVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: LongNavDto by navDto()

    private val chipIdToProfileId = mutableMapOf<Int, Long>()
    private var profileId: Long? = null

    private val adapter: HealthDiaryAdapter =
        HealthDiaryAdapter(
            expandItem = { vm.handleStepClick(it) },
            addSurvey = { openAddingSurveyDialog(it) },
            onDelete = { openDeleteDialog(it.id) }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        viewBinding.marksGroup.setOnCheckedChangeListener { _, checkedId ->
            profileId = chipIdToProfileId[checkedId]
            vm.profileId.value = profileId
            vm.getHealthDiaryItems()
        }

        with(viewBinding.recyclerView) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@HealthDiaryFragment.adapter
        }
    }

    private fun openAddingSurveyDialog(item: ItemHealthDiary) {
        val binding = DialogHealthDiarySurveyBinding.inflate(LayoutInflater.from(context))
        val dialog = MaterialDialog(requireContext())
            .customView(view = binding.root)
            .positiveButton(R.string.save) {
                vm.createHealthDiarySurvey(
                    healthDiarySurveyBuilder(
                        binding = binding,
                        item = item
                    )
                )
            }
            .negativeButton(R.string.cancel) { it.dismiss() }

        val (title: Int, measure: Int, isBloodPressureType: Boolean) = when (item.type) {
            HealthDiaryType.BLOOD_PRESSURE -> Triple(
                R.string.health_diary_survey_blood_pressure,
                R.string.health_diary_blood_pressure_unit,
                true
            )
            HealthDiaryType.PULSE -> Triple(
                R.string.health_diary_survey_pulse,
                R.string.health_diary_pulse_unit,
                false
            )
            HealthDiaryType.BLOOD_GLUCOSE -> Triple(
                R.string.health_diary_survey_blood_glucose,
                R.string.health_diary_blood_glucose_unit,
                false
            )
            HealthDiaryType.HEIGHT -> Triple(
                R.string.health_diary_survey_height,
                R.string.health_diary_height_unit,
                false
            )
            HealthDiaryType.WEIGHT -> Triple(
                R.string.health_diary_survey_weight,
                R.string.health_diary_weight_unit,
                false
            )
        }
        with(binding) {
            initDateAndTime(this)
            initDialogConstraints(this, isBloodPressureType)
            bloodPressureSurvey.isVisible = isBloodPressureType
            surveyValue.isVisible = !isBloodPressureType

            surveyValueTitle.text = getString(title)
            unitOfMeasure.text = getString(measure)
        }
        dialog.show()
    }

    private fun initDialogConstraints(binding: DialogHealthDiarySurveyBinding, isBloodPressureType: Boolean) {
        with(ConstraintSet()) {
            clone(binding.dialogContent)
            if (isBloodPressureType) {
                connect(
                    R.id.unit_of_measure,
                    ConstraintSet.BOTTOM,
                    R.id.blood_pressure_second,
                    ConstraintSet.BOTTOM
                )
                connect(R.id.unit_of_measure, ConstraintSet.START, R.id.blood_pressure_second, ConstraintSet.END)
            } else {
                connect(R.id.unit_of_measure, ConstraintSet.BOTTOM, R.id.survey_value, ConstraintSet.BOTTOM)
                connect(R.id.unit_of_measure, ConstraintSet.START, R.id.survey_value, ConstraintSet.END)
            }
            applyTo(binding.dialogContent)
        }
    }

    private fun healthDiarySurveyBuilder(
        binding: DialogHealthDiarySurveyBinding,
        item: ItemHealthDiary
    ): SurveyHealthDiary {
        with(binding) {
            val id = DEFAULT_ID_FOR_ENTITY
            val type = item.type
            val profileId = profileId ?: DEFAULT_ID_FOR_ENTITY
            val date = surveyDate.text.toString()
            val time = surveyTime.text.toString()
            val measure = unitOfMeasure.text.toString()
            val surveyValue = if (type == HealthDiaryType.BLOOD_PRESSURE) {
                StringBuilder()
                    .append(bloodPressureFirst.text.toString())
                    .append(getString(R.string.health_diary_slash))
                    .append(bloodPressureSecond.text.toString())
                    .toString()
            } else {
                surveyValue.text.toString()
            }

            return SurveyHealthDiary(
                id = id,
                type = type,
                profileId = profileId,
                date = date,
                time = time,
                surveyValue = surveyValue,
                unitOfMeasure = measure
            )
        }
    }

    private fun initObservers() {
        with(vm) {
            profiles.observe(viewLifecycleOwner, { profiles ->
                if (profiles.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (profile in profiles) {
                        val chip = createChip(requireContext(), profile, SurveysFragment.CHIP_PADDING)
                        viewBinding.marksGroup.addView(chip)
                        chipIdToProfileId[chip.id] = profile.id
                    }
                    if (args.value != 0L) {
                        val index = chipIdToProfileId.filterValues { it == args.value }.keys.first()
                        viewBinding.marksGroup.post {
                            viewBinding.marksGroup.check(index)
                            val chip = viewBinding.marksGroup.findViewById<Chip>(viewBinding.marksGroup.checkedChipId)
                            viewBinding.markGroupContainer.smoothScrollTo(chip.left, chip.top)
                        }
                    } else {
                        (viewBinding.marksGroup[0] as? Chip)?.isChecked = true
                    }
                }
            })
            healthDiaryItems.observe(viewLifecycleOwner, {
                adapter.items = it.toPresentationModel()
            })
            errorMessage.observe(viewLifecycleOwner, { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun openDeleteDialog(surveyId: Long) {
        MaterialDialog(requireActivity()).show {
            title(text = getString(R.string.health_diary_delete_survey))
            positiveButton {
                vm.deleteHealthDiary(surveyId)
                cancel()
            }
            negativeButton { cancel() }
        }
    }

    private fun showAlertEmptyProfiles() {
        MaterialDialog(requireActivity())
            .cancelable(false)
            .show {
                title(text = getString(R.string.survey_incomplete_data))
                positiveButton { vm.openProfileScreen() }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDateAndTime(binding: DialogHealthDiarySurveyBinding) {

        val date = Calendar.getInstance()
        with(binding) {
            surveyDate.text = SimpleDateFormat("dd.MM.yyyy").format(date.time)
            surveyTime.text = SimpleDateFormat("HH:mm").format(date.time)

            surveyDate.setOnClickListener {
                val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    date.set(year, month, dayOfMonth)
                    surveyDate.text = SimpleDateFormat("dd.MM.yyyy").format(date.time)
                }

                DatePickerDialog(
                    requireActivity(),
                    listener,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            surveyTime.setOnClickListener {
                val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    date.set(hourOfDay, minute)
                    surveyTime.text = SimpleDateFormat("HH:mm").format(date.time)
                }

                TimePickerDialog(
                    requireActivity(),
                    listener,
                    date.get(Calendar.HOUR),
                    date.get(Calendar.MINUTE),
                    true
                ).show()
            }
        }
    }

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = 0L
    }
}
