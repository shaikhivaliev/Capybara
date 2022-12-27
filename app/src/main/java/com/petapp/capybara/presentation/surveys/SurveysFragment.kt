package com.petapp.capybara.presentation.surveys

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.LongNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.*
import javax.inject.Inject

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    @Inject
    lateinit var vmFactory: SurveysVmFactory

    private val vm: SurveysVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: LongNavDto by navDto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            args.value?.let { vm.getMarks(it) }
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
        val surveysState by vm.surveysState.observeAsState()

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.openSurveyScreen(null)
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            },
            content = {
                when (val state = surveysState) {
                    DataState.EMPTY -> EmptyData(stringResource(R.string.survey_mock))
                    DataState.ACTION -> showAlertEmptyProfiles()
                    is DataState.ERROR -> ShowError()
                    is DataState.DATA -> ShowSurveys(state.data)
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowSurveys(surveys: SurveysState) {
        // todo
        val selectedChip by remember { mutableStateOf(false) }

        Column {
            ChipRow(chips = surveys.profiles.toUiData(
                click = {
                    vm.getCheckedSurveys(it)
                }
            ))
            StandardColumn {
                items(surveys.checkedSurveys) { item ->
                    CircleIconTitleDescItem(
                        onItemClick = { vm.openSurveyScreen(item) },
                        item = item.toUiData(),
                        modifier = modifierCircleIcon76dp()
                    )
                }
            }
        }
    }

    @Composable
    private fun ShowError() {
//        LaunchedEffect(scaffoldState.snackbarHostState) {
//            // Show snackbar using a coroutine, when the coroutine is cancelled the
//            // snackbar will automatically dismiss. This coroutine will cancel whenever
//            // `state.hasError` is false, and only start when `state.hasError` is true
//            // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
//            scaffoldState.snackbarHostState.showSnackbar(
//                message = "Error message",
//                actionLabel = "Retry message"
//            )
//        }
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
