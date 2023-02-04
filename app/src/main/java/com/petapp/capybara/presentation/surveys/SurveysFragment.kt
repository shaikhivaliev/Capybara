package com.petapp.capybara.presentation.surveys

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.navigation.LongNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.*
import javax.inject.Inject

class SurveysFragment : Fragment() {

    @Inject
    lateinit var vmFactory: SurveysVmFactory

    private val vm: SurveysVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: LongNavDto? by navDto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            vm.getProfiles(args?.value)
            setContent {
                MdcTheme {
                    SurveysScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun SurveysScreen() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val surveysState by vm.surveysState.collectAsState()
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.openNewSurveyScreen()
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            },
            content = {
                when (val state = surveysState) {
                    DataState.EMPTY -> showAlertEmptyProfiles()
                    is DataState.DATA -> ShowSurveys(state.data)
                    is DataState.ERROR -> Error()
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowSurveys(surveys: SurveysState) {
        Column {
            ChipLazyRow(
                chips = surveys.profiles.toUiData(
                    selectedChipId = surveys.profiles.first().id,
                    click = {
                        vm.getCheckedSurveys(it)
                    }
                ))
            if (surveys.checkedSurveys.isEmpty()) {
                Empty(stringResource(R.string.survey_mock))
            } else {
                BaseLazyColumn {
                    items(surveys.checkedSurveys) { item ->
                        IconTitleDescItem(
                            onClick = { vm.openSurveyScreen(item) },
                            item = item.toUiData(),
                            contentScale = ContentScale.Inside
                        )
                    }
                }
            }
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
}
