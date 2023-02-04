package com.petapp.capybara.presentation.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.ChipLazyRow
import com.petapp.capybara.ui.Error
import com.petapp.capybara.ui.rememberStaticSelectionState
import io.github.boguszpawlowski.composecalendar.Calendar
import javax.inject.Inject

class CalendarFragment : Fragment() {

    @Inject
    lateinit var vmFactory: CalendarVmFactory

    private val vm: CalendarVm by stateViewModel(
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
                    CalendarScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun CalendarScreen() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val calendarState by vm.calendarState.collectAsState()
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
                when (val state = calendarState) {
                    DataState.EMPTY -> showAlertEmptyProfiles()
                    is DataState.DATA -> ShowCalendar(state.data)
                    is DataState.ERROR -> Error()
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowCalendar(calendarUI: CalendarUI) {
        val state = rememberStaticSelectionState()
        state.selectionState.selection = calendarUI.checkedSurveysDates
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            ChipLazyRow(
                chips = calendarUI.profiles.toUiData(
                    selectedChipId = calendarUI.checkedProfileId,
                    click = {
                        vm.getCheckedSurveys(it)
                    }
                ))
            Calendar(calendarState = state)
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
