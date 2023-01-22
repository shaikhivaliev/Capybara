package com.petapp.capybara.presentation.healthDiary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.core.state.DataState
import com.petapp.capybara.core.state.observeData
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.databinding.DialogHealthDiarySurveyBinding
import com.petapp.capybara.databinding.FragmentHealthDiaryBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toPresentationModel
import com.petapp.capybara.ui.openAddingSurveyDialog
import javax.inject.Inject

class HealthDiaryFragment : Fragment(R.layout.fragment_health_diary) {

    private val viewBinding by viewBinding(FragmentHealthDiaryBinding::bind)

    @Inject
    lateinit var vmFactory: HealthDiaryVmFactory

    private val vm: HealthDiaryVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val chipIdToProfileId = mutableMapOf<Int, Long>()
    private var profileId: Long? = null

    private val adapter: HealthDiaryAdapter =
        HealthDiaryAdapter(
            expandItem = {
                vm.expandItem(it)
            },
            addSurvey = {
                openAddingSurveyDialog(
                    context = requireContext(),
                    item = it,
                    onClick = { binding ->
                        vm.createHealthDiarySurvey(
                            healthDiarySurveyBuilder(
                                binding = binding,
                                item = it
                            )
                        )
                    }
                )
            },
            onDelete = { openDeleteDialog(it.id) }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        viewBinding.marksGroup.setOnCheckedChangeListener { _, checkedId ->
            val profileId = chipIdToProfileId[checkedId]
            profileId?.let { vm.getHealthDiaryItemsByProfile(profileId) }
        }

        with(viewBinding.recyclerView) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@HealthDiaryFragment.adapter
        }
    }

    private fun initObservers() {
        observeData(vm.healthDiaryState) { state ->
            when (state) {
                DataState.EMPTY -> showAlertEmptyProfiles()
                is DataState.DATA -> {
                    if (chipIdToProfileId.isEmpty()) {
                        initChips(state.data)
                    }
                    showHealthDiary(state.data)
                }
                is DataState.ERROR -> showError()
                else -> { // nothing
                }
            }
        }
    }

    private fun initChips(data: HealthDiaryUI) {
        for (profile in data.profiles) {
            val chip = createChip(requireContext(), profile, CHIP_PADDING)
            viewBinding.marksGroup.addView(chip)
            chipIdToProfileId[chip.id] = profile.id
        }
    }

    private fun showHealthDiary(data: HealthDiaryUI) {
        checkChip(data.checkedProfileId)
        adapter.items = data.healthDiaryList.toPresentationModel()
    }

    private fun checkChip(checkedProfileId: Long) {
        val index = chipIdToProfileId.filterValues { it == checkedProfileId }.keys.first()
        viewBinding.marksGroup.check(index)
    }

    private fun showError() {
        // todo
    }

    // todo move to vm
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

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = 0L
        private const val CHIP_PADDING = 56F
    }
}
