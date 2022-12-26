package com.petapp.capybara.presentation.surveys

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.LongNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.CircleIconTitleDescItem
import com.petapp.capybara.ui.EmptyData
import com.petapp.capybara.ui.modifierBaseList
import com.petapp.capybara.ui.modifierCircleIcon76dp
import javax.inject.Inject

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    @Inject
    lateinit var vmFactory: SurveysVmFactory

    private val vm: SurveysVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: LongNavDto by navDto()
    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    SurveysScreen()
                }
            }
        }
    }

    @Composable
    fun SurveysScreen() {
//        viewBinding.addSurvey.setOnClickListener { vm.openSurveyScreen(null) }
//        viewBinding.marksGroup.setOnCheckedChangeListener { _, checkedId ->
//            vm.profileId.value = chipIdToProfileId[checkedId]
//            args.value?.let { vm.getSurveys(it) }
//        }
//        profiles.observe(viewLifecycleOwner) { profiles ->
//            if (profiles.isEmpty()) {
//                showAlertEmptyProfiles()
//            } else {
//                for (profile in profiles) {
//                    val chip = createChip(requireContext(), profile, CHIP_PADDING)
//                    viewBinding.marksGroup.addView(chip)
//                    chipIdToProfileId[chip.id] = profile.id
//                }
//                (viewBinding.marksGroup[0] as? Chip)?.isChecked = true
//            }
//        }

        val surveysState by vm.surveysState.observeAsState()

        Column {
            // todo chips
            when (val state = surveysState) {
                DataState.EMPTY -> EmptyData(stringResource(R.string.survey_mock))
                DataState.ACTION -> showAlertEmptyProfiles()
                is DataState.ERROR -> ShowError()
                is DataState.DATA -> ShowSurveys(state.data)
                else -> { // nothing
                }
            }
        }
    }

    @Composable
    private fun ShowSurveys(surveys: List<Survey>) {
        LazyColumn(
            modifier = modifierBaseList(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(surveys) { item ->
                    CircleIconTitleDescItem(
                        onItemClick = { vm.openSurveyScreen(item) },
                        item = item.toUiData(),
                        modifier = modifierCircleIcon76dp()
                    )
                }
            })
    }

    @Composable
    private fun ShowError() {
        // todo
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
        const val CHIP_PADDING = 56F
    }
}
