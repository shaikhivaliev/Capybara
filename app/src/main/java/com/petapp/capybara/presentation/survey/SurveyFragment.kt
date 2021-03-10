package com.petapp.capybara.presentation.survey

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.getListAdapter
import com.afollestad.materialdialogs.list.getRecyclerView
import com.petapp.capybara.R
import com.petapp.capybara.common.MarginItemDecoration
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.currentDateMonthYear
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_survey.*
import kotlinx.android.synthetic.main.item_survey.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class SurveyFragment : Fragment(R.layout.fragment_survey) {

    private val viewModel: SurveyViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: SurveyFragmentArgs by navArgs()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        initWorkWithDate()

        args.survey?.id?.apply { viewModel.getSurvey(this) }

        if (args.survey?.id == null) {
            current_survey.isVisible = false
            edit_survey.isVisible = true
            delete_survey.isVisible = false
            survey_name_et.requestFocus()
            survey_name_et.showKeyboard()
            survey_date_et.setText(dateFormat.format(calendar.time))
        }
    }

    private fun initViews() {
        delete_survey.setOnClickListener { deleteSurvey() }

        done.setOnClickListener {
            if (args.survey != null) {
                viewModel.updateSurvey(surveyBuilder())
            } else {
                viewModel.createSurvey(surveyBuilder())
            }
        }
        edit.setOnClickListener {
            current_survey.isVisible = false
            edit_survey.isVisible = true
            delete_survey.isVisible = true
            survey_name_et.setText(args.survey?.name)
            survey_date_et.setText(args.survey?.date)
        }

        survey_name_et.doAfterTextChanged { survey_name_layout.error = null }
        survey_date_et.doAfterTextChanged { survey_date_layout.error = null }
    }

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

    private fun initObservers() {
        with(viewModel) {
            survey.observe(viewLifecycleOwner, Observer { survey ->
                setSurvey(survey)
            })
            types.observe(viewLifecycleOwner, Observer { types ->
                change_survey_type.setOnClickListener { showChangeTypeDialog(types) }
                if (args.survey?.typeId != null) {
                    val type = types.find { it.id == args.survey?.typeId }
                    // type?.let { type_icon.setImageResource(it.icon) }
                }
            })
            profiles.observe(viewLifecycleOwner, Observer { profiles ->
                change_survey_profile.setOnClickListener { showChangeProfileDialog(profiles) }
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
//            currentProfile.observe(viewLifecycleOwner, Observer { profile ->
//                profile_mark.setBackgroundColor(profile.color)
//            })
//            currentType.observe(viewLifecycleOwner, Observer { type ->
//                 type_icon.setImageResource(type.icon)
//            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initWorkWithDate() {

        survey_date_et.setOnClickListener {
            survey_date_et.requestFocus()
            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                survey_date_et.setText(dateFormat.format(calendar.time))
            }

            DatePickerDialog(
                requireActivity(),
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setSurvey(survey: Survey) {
        // profile_mark.setBackgroundColor(survey.color)
        survey_name.text = survey.name
        survey_date.text = survey.date
    }

    private fun surveyBuilder(): Survey? {
        return if (isFieldsValid()) {
            val id = args.survey?.id ?: DEFAULT_ID_FOR_ENTITY
            val typeId = requireNotNull(currentType.value?.id)
            val profileId = requireNotNull(currentProfile.value?.id)
            val color = requireNotNull(currentProfile.value?.color)
            val profileIcon = requireNotNull(currentProfile.value?.photo)
            val name = survey_name_et.text.toString()
            val date = survey_date_et.text.toString()
            val time = dateFormat.parse(date)
            calendar.time = time!!
            val monthYear = currentDateMonthYear(calendar.time)
            return Survey(
                id = id,
                typeId = typeId,
                profileId = profileId,
                color = color,
                name = name,
                date = date,
                monthYear = monthYear,
                profileIcon = profileIcon
            )
        } else {
            null
        }
    }

    private fun deleteSurvey() {
        val name = survey_name_et.text.toString()
        MaterialDialog(requireActivity()).show {
            if (name.isNotBlank()) {
                title(text = getString(R.string.survey_delete_explanation, name))
            } else {
                title(text = getString(R.string.survey_delete_explanation_empty))
            }
            positiveButton {
                if (args.survey?.id != null) {
                    viewModel.deleteSurvey(args.survey?.id!!)
                } else {
                    viewModel.back()
                }
                cancel()
            }
            negativeButton { cancel() }
        }
    }

    private fun isFieldsValid(): Boolean {
        return isNameValid() && isDateValid() && isTypeSelected() && isProfileSelected()
    }

    private fun isNameValid(): Boolean {
        return if (survey_name_et.text.toString().isNotBlank()) true
        else {
            survey_name_layout.error = requireActivity().getString(R.string.error_empty_name)
            false
        }
    }

    private fun isDateValid(): Boolean {
        return if (survey_date_et.text.toString().isNotBlank()) true
        else {
            survey_date_layout.error = requireActivity().getString(R.string.error_empty_date)
            false
        }
    }

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
        const val MARGIN_TOP_BOTTOM = 14
        const val MARGIN_START_END = 24
        const val PADDING_START = 24
        const val DEFAULT_ID_FOR_ENTITY = 0L
    }
}
