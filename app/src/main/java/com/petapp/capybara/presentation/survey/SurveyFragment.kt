package com.petapp.capybara.presentation.survey

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.SideEffect
import com.petapp.capybara.core.navigation.SurveyNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.ui.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SurveyFragment : Fragment() {

    @Inject
    lateinit var vmFactory: SurveyVmFactory

    private val vm: SurveyVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val args: SurveyNavDto? by navDto()

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            vm.getSurvey(args?.survey)
            setContent {
                MdcTheme {
                    SurveyScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun SurveyScreen() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val surveyState by vm.surveyState.observeAsState()
        val sideEffect by vm.sideEffect.observeAsState()
        val surveyInputData = SurveyInputData()

        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                ShowFab(surveyState, surveyInputData)
            },
            content = {
                when (val state = surveyState) {
                    is DataState.DATA -> {
                        ShowSurvey(state.data, surveyInputData)
                    }
                    is DataState.ERROR -> Error()
                    else -> { // nothing
                    }
                }

                if (sideEffect is SideEffect.ACTION) {
                    // todo fix repeat showing
                    ShowSnackbar(
                        scaffoldState = scaffoldState,
                        errorMessage = stringResource(R.string.error_empty_data)
                    )
                }
            }
        )
    }

    @Composable
    private fun ShowFab(surveyState: DataState<SurveyMode>?, surveyInputData: SurveyInputData) {
        surveyState?.onData { mode ->
            FloatingActionButton(
                onClick = {
                    when (mode) {
                        is SurveyMode.NEW, is SurveyMode.EDIT -> vm.verifySurvey(
                            mode,
                            surveyInputData
                        )
                        is SurveyMode.READONLY -> vm.toEditMode(mode.data)
                    }
                }) {
                val fabImage = when (mode) {
                    is SurveyMode.NEW -> ImageVector.vectorResource(R.drawable.ic_done)
                    is SurveyMode.EDIT -> ImageVector.vectorResource(R.drawable.ic_done)
                    is SurveyMode.READONLY -> ImageVector.vectorResource(R.drawable.ic_edit)
                }
                Icon(
                    imageVector = fabImage,
                    contentDescription = null
                )
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun ShowSurvey(mode: SurveyMode, surveyInputData: SurveyInputData) {
        when (mode) {
            is SurveyMode.NEW -> SurveyContent(
                profiles = mode.data.profiles.map { it.name },
                types = mode.data.types.map { it.name },
                surveyTitle = surveyInputData.survey,
                dateTitle = surveyInputData.date,
                profileTitle = surveyInputData.profile,
                typeTitle = surveyInputData.type,
                isEditMode = false
            )
            is SurveyMode.EDIT -> {
                with(mode.data) {
                    surveyInputData.survey.value = survey.name
                    surveyInputData.date.value = survey.date
                    surveyInputData.profile.value = profileTitle
                    surveyInputData.type.value = typeTitle
                }
                SurveyContent(
                    profiles = mode.data.profiles.map { it.name },
                    types = mode.data.types.map { it.name },
                    surveyTitle = surveyInputData.survey,
                    dateTitle = surveyInputData.date,
                    profileTitle = surveyInputData.profile,
                    typeTitle = surveyInputData.type,
                    isEditMode = true
                )
            }
            is SurveyMode.READONLY -> SurveyContentReadOnly(mode.data)
        }
    }

    @Composable
    fun SurveyContent(
        profiles: List<String>,
        types: List<String>,
        surveyTitle: MutableState<String>,
        dateTitle: MutableState<String>,
        profileTitle: MutableState<String>,
        typeTitle: MutableState<String>,
        isEditMode: Boolean
    ) {
        val profilesMenuExpanded = remember { mutableStateOf(false) }
        val typesMenuExpanded = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = surveyTitle.value,
                onValueChange = {
                    surveyTitle.value = it
                },
                label = { Text(stringResource(R.string.survey)) }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = dateTitle.value,
                onValueChange = {
                    dateTitle.value = it
                },
                label = { Text(stringResource(R.string.health_diary_survey_date)) }
            )
            ExpandedDropdownMenu(
                label = stringResource(R.string.profile_caps),
                selectedTitle = profileTitle.value,
                expanded = profilesMenuExpanded.value,
                selectionOptions = profiles,
                onExpandedChange = {
                    profilesMenuExpanded.value = !profilesMenuExpanded.value
                },
                onSelectedText = {
                    profileTitle.value = it
                }
            )
            ExpandedDropdownMenu(
                label = stringResource(R.string.survey_change_survey_type),
                selectedTitle = typeTitle.value,
                expanded = typesMenuExpanded.value,
                selectionOptions = types,
                onExpandedChange = {
                    typesMenuExpanded.value = !typesMenuExpanded.value
                },
                onSelectedText = {
                    typeTitle.value = it
                }
            )
            if (isEditMode) {
                TextButton(onClick = { deleteSurvey() }) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        text = stringResource(R.string.survey_delete),
                        color = Color.Red,
                        style = textSmall(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    @Composable
    fun SurveyContentReadOnly(data: SurveyUI) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        ) {
            OutlinedTextFieldReadOnly(
                value = data.survey.name,
                label = stringResource(R.string.survey)
            )
            OutlinedTextFieldReadOnly(
                value = data.survey.date,
                label = stringResource(R.string.health_diary_survey_date)
            )
            ExpandedDropdownMenuReadOnly(
                value = data.profileTitle,
                label = stringResource(R.string.profile_caps)
            )
            ExpandedDropdownMenuReadOnly(
                value = data.typeTitle,
                label = stringResource(R.string.survey_change_survey_type)
            )
        }
    }

    private fun deleteSurvey() {
        // todo
        val name = "name"
        MaterialDialog(requireActivity()).show {
            if (name.isNotBlank()) {
                title(text = getString(R.string.survey_delete_explanation, name))
            } else {
                title(text = getString(R.string.survey_delete_explanation_empty))
            }
            positiveButton {
                args?.survey?.id?.let { vm.deleteSurvey(it) } ?: vm.back()
                cancel()
            }
            negativeButton { cancel() }
        }
    }


//    private fun initWorkWithDate() {
//        viewBinding.surveyDateEt.setOnClickListener {
//            viewBinding.surveyDateEt.requestFocus()
//            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
//                calendar.set(year, month, dayOfMonth)
//                viewBinding.surveyDateEt.setText(dateFormat.format(calendar.time))
//            }
//
//            DatePickerDialog(
//                requireActivity(),
//                listener,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
//    }

}
