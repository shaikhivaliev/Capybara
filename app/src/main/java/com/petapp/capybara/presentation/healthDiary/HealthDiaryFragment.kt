package com.petapp.capybara.presentation.healthDiary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.healthDiary.HealthDiaryType
import com.petapp.capybara.data.model.healthDiary.ItemHealthDiary
import com.petapp.capybara.data.model.healthDiary.SurveyHealthDiary
import com.petapp.capybara.databinding.DialogHealthDiarySurveyBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.*
import com.petapp.capybara.ui.dialogs.openAddingSurveyDialog
import com.petapp.capybara.ui.dialogs.showAlertEmptyProfiles
import com.petapp.capybara.ui.list.*
import com.petapp.capybara.ui.state.Error
import com.petapp.capybara.ui.styles.textSmall
import javax.inject.Inject

class HealthDiaryFragment : Fragment() {

    @Inject
    lateinit var vmFactory: HealthDiaryVmFactory

    private val vm: HealthDiaryVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    HealthDiaryScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun HealthDiaryScreen() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val healthDiaryState by vm.healthDiaryState.collectAsState()
        Scaffold(
            scaffoldState = scaffoldState,
            content = {
                when (val state = healthDiaryState) {
                    DataState.EMPTY -> showAlertEmptyProfiles { vm.openProfileScreen() }
                    is DataState.DATA -> ShowHealthDiary(state.data)
                    is DataState.ERROR -> Error()
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowHealthDiary(state: HealthDiaryUI) {
        Column {
            ChipLazyRow(
                chips = state.profiles.toUiData(
                    selectedChipId = state.checkedProfileId,
                    click = {
                        vm.getHealthDiaryItemsByProfile(it)
                    }
                ))
            BaseLazyColumn {
                state.checkedHealthDiary.forEach { healthDiary ->
                    item {
                        HealthDiaryHeader(
                            item = healthDiary,
                            expand = { vm.expandItem(healthDiary) },
                            addNew = {
                                openAddingSurveyDialog(
                                    context = requireContext(),
                                    item = healthDiary,
                                    onClick = { binding ->
                                        vm.createHealthDiarySurvey(
                                            healthDiarySurveyBuilder(
                                                binding = binding,
                                                item = healthDiary,
                                                profileId = state.checkedProfileId
                                            )
                                        )
                                    }
                                )
                            }
                        )
                    }
                    if (healthDiary.isExpanded && healthDiary.surveys.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.health_diary_empty_survey),
                                style = textSmall()
                            )
                        }
                    }
                    if (healthDiary.isExpanded && healthDiary.surveys.isNotEmpty()) {
                        items(healthDiary.surveys) { item ->
                            HealthDiarySurveyItem(
                                item = item,
                                deleteSurvey = { openDeleteDialog(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun healthDiarySurveyBuilder(
        binding: DialogHealthDiarySurveyBinding,
        item: ItemHealthDiary,
        profileId: Long
    ): SurveyHealthDiary {
        with(binding) {
            val surveyValue = if (item.type == HealthDiaryType.BLOOD_PRESSURE) {
                StringBuilder()
                    .append(bloodPressureFirst.text.toString())
                    .append(getString(R.string.health_diary_slash))
                    .append(bloodPressureSecond.text.toString())
                    .toString()
            } else {
                surveyValue.text.toString()
            }

            return SurveyHealthDiary(
                id = 0L,
                type = item.type,
                profileId = profileId,
                date = surveyDate.text.toString(),
                time = surveyTime.text.toString(),
                surveyValue = surveyValue,
                unitOfMeasure = unitOfMeasure.text.toString()
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
}
