package com.petapp.capybara.presentation.survey

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.list.MarginItemDecoration
import com.petapp.capybara.core.navigation.SurveyNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.ui.ShowError
import com.petapp.capybara.ui.textLarge
import com.petapp.capybara.ui.textMedium
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

    private val adapterTypesDialog: TypesDialogAdapter = TypesDialogAdapter(
        itemClick = { type ->
            currentType.value = type
            typeIconDialog?.cancel()
        }
    )

    private val adapterProfilesDialog: ProfilesDialogAdapter = ProfilesDialogAdapter(
        itemClick = { profile ->
            currentProfile.value = profile
            profileMarkDialog?.cancel()
        }
    )

    private val currentProfile = MutableLiveData<Profile>()
    private val currentType = MutableLiveData<Type>()

    private var typeIconDialog: MaterialDialog? = null
    private var profileMarkDialog: MaterialDialog? = null

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
        Scaffold(
            scaffoldState = scaffoldState,
            content = {
                when (val state = surveyState) {
                    is DataState.DATA -> ShowSurvey(state.data)
                    is DataState.ERROR -> ShowError(
                        scaffoldState = scaffoldState,
                        errorMessage = stringResource(R.string.error_explanation),
                        action = { vm.getSurvey(args?.survey) }
                    )
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowSurvey(mode: SurveyMode) {
        when (mode) {
            is SurveyMode.NEW -> ShowNewMode()
            is SurveyMode.EDIT -> ShowEditMode(mode.survey)
            is SurveyMode.OPEN -> ShowOpenMode(mode.survey)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun ShowNewMode() {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // todo
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_done),
                        contentDescription = null
                    )
                }
            },
            content = {
            }
        )
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun ShowEditMode(survey: Survey) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // todo
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_done),
                        contentDescription = null
                    )
                }
            },
            content = {
            }
        )
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun ShowOpenMode(survey: Survey) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.toEditMode(survey)
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_edit),
                        contentDescription = null
                    )
                }
            },
            content = {
                Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
                    Text(
                        text = survey.name,
                        style = textLarge()
                    )
                    Text(
                        text = survey.date,
                        style = textMedium()
                    )
                }
            }
        )
    }


//        if (args.survey?.id == null) {
//            viewBinding.currentSurvey.isVisible = false
//            viewBinding.editSurvey.isVisible = true
//            viewBinding.deleteSurvey.isVisible = false
//            viewBinding.surveyNameEt.requestFocus()
//            viewBinding.surveyNameEt.showKeyboard()
//            viewBinding.surveyDateEt.setText(dateFormat.format(calendar.time))
//            viewBinding.deleteSurvey.setOnClickListener { deleteSurvey() }
//
//            viewBinding.done.setOnClickListener {
//                if (args.survey != null) {
//                    vm.updateSurvey(surveyBuilder())
//                } else {
//                    vm.createSurvey(surveyBuilder())
//                }
//            }
//            viewBinding.edit.setOnClickListener {
//                viewBinding.currentSurvey.isVisible = false
//                viewBinding.editSurvey.isVisible = true
//                viewBinding.deleteSurvey.isVisible = true
//                viewBinding.surveyNameEt.setText(args.survey?.name)
//                viewBinding.surveyDateEt.setText(args.survey?.date)
//            }
//
//            viewBinding.surveyNameEt.doAfterTextChanged { viewBinding.surveyNameLayout.error = null }
//            viewBinding.surveyDateEt.doAfterTextChanged { viewBinding.surveyDateLayout.error = null }
//private fun initObservers() {
//    with(vm) {
//        survey.observe(viewLifecycleOwner, { survey ->
//            setSurvey(survey)
//        })
//        types.observe(viewLifecycleOwner, { types ->
//            viewBinding.changeSurveyType.setOnClickListener { showChangeTypeDialog(types) }
//            if (args.survey?.typeId != null) {
//                currentType.value = types.find { it.id == args.survey?.typeId }
//            }
//        })
//        profiles.observe(viewLifecycleOwner, { profiles ->
//            viewBinding.changeSurveyProfile.setOnClickListener { showChangeProfileDialog(profiles) }
//            if (args.survey?.profileId != null) {
//                currentProfile.value = profiles.find { it.id == args.survey?.profileId }
//            }
//        })
//        currentProfile.observe(viewLifecycleOwner, { profile ->
//            viewBinding.changeSurveyProfile.text = profile.name
//        })
//        currentType.observe(viewLifecycleOwner, { type ->
//            viewBinding.changeSurveyType.text = type.name
//        })
//    }
//}

    private fun showChangeTypeDialog(types: List<Type>) {
        adapterTypesDialog.items = types

        typeIconDialog = MaterialDialog(requireActivity()).show {
            title(R.string.type_caps)
            customListAdapter(adapterTypesDialog)
            val itemCount = getListAdapter()?.itemCount ?: 0
            getRecyclerView().addItemDecoration(
                MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_ml), itemCount - 1)
            )
        }
    }

    private fun showChangeProfileDialog(marks: List<Profile>) {
        adapterProfilesDialog.items = marks

        profileMarkDialog = MaterialDialog(requireActivity()).show {
            title(R.string.profile_caps)
            customListAdapter(adapterProfilesDialog)
            val itemCount = getListAdapter()?.itemCount ?: 0
            getRecyclerView().addItemDecoration(
                MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_s), itemCount - 1)
            )
        }
    }

    private fun initWorkWithDate() {
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
    }

//    private fun surveyBuilder(): Survey {
//        return if (isFieldsValid()) {
//            val id = args.survey?.id ?: DEFAULT_ID_FOR_ENTITY
//            val typeId = requireNotNull(currentType.value?.id)
//            val profileId = requireNotNull(currentProfile.value?.id)
//            val color = requireNotNull(currentProfile.value?.color)
//            val profileIcon = requireNotNull(currentProfile.value?.photo)
//            val typeIcon = requireNotNull(currentType.value?.icon)
//            val name = viewBinding.surveyNameEt.text.toString()
//            val date = viewBinding.surveyDateEt.text.toString()
//            val time = dateFormat.parse(date)
//            calendar.time = time!!
//            val monthYear = currentDateMonthYear(calendar.time)
//            return Survey(
//                id = id,
//                typeId = typeId,
//                profileId = profileId,
//                color = color,
//                name = name,
//                date = date,
//                monthYear = monthYear,
//                profileIcon = profileIcon,
//                typeIcon = typeIcon
//            )
//        } else {
//            null
//        }
//    }

    private fun deleteSurvey() {
//        val name = viewBinding.surveyNameEt.text.toString()
//        MaterialDialog(requireActivity()).show {
//            if (name.isNotBlank()) {
//                title(text = getString(R.string.survey_delete_explanation, name))
//            } else {
//                title(text = getString(R.string.survey_delete_explanation_empty))
//            }
//            positiveButton {
//                if (args.survey?.id != null) {
//                    vm.deleteSurvey(args.survey?.id!!)
//                } else {
//                    vm.back()
//                }
//                cancel()
//            }
//            negativeButton { cancel() }
//        }
    }

//    private fun isFieldsValid(): Boolean {
//        return isNameValid() && isDateValid() && isTypeSelected() && isProfileSelected()
//    }

//    private fun isNameValid(): Boolean {
//        return if (viewBinding.surveyNameEt.text.toString().isNotBlank()) true
//        else {
//            viewBinding.surveyNameLayout.error = requireActivity().getString(R.string.error_empty_name)
//            false
//        }
//    }

//    private fun isDateValid(): Boolean {
//        return if (viewBinding.surveyDateEt.text.toString().isNotBlank()) true
//        else {
//            viewBinding.surveyDateLayout.error = requireActivity().getString(R.string.error_empty_date)
//            false
//        }
//    }

    private fun isTypeSelected(): Boolean {
        return if (currentType.value != null) true
        else {
            requireActivity().toast(R.string.error_empty_type)
            false
        }
    }

    private fun isProfileSelected(): Boolean {
        return if (currentProfile.value != null) true
        else {
            requireActivity().toast(R.string.error_empty_profile)
            false
        }
    }

    companion object {
        const val DEFAULT_ID_FOR_ENTITY = 0L
    }
}
