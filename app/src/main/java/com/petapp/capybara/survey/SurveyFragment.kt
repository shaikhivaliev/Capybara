package com.petapp.capybara.survey

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Mark
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.currentMonth
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_survey.*
import kotlinx.android.synthetic.main.fragment_survey.done
import kotlinx.android.synthetic.main.fragment_survey.edit
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class SurveyFragment : Fragment(R.layout.fragment_survey) {

    private val viewModel: SurveyViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: SurveyFragmentArgs by navArgs()

    private val adapterTypesDialog: TypesDialogAdapter by lazy { TypesDialogAdapter() }

    private val adapterMarksDialog: MarksDialogAdapter by lazy { MarksDialogAdapter() }

    private val currentProfileMark = MutableLiveData<Mark>()
    private val currentType = MutableLiveData<Type>()

    private var typeIconDialog: MaterialDialog? = null
    private var profileMarkDialog: MaterialDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObservers()
        initWorkWithDate()

        args.survey?.id?.apply { viewModel.getSurvey(this) }

        if (args.survey?.id == null) {
            current_survey.visible(false)
            edit_survey.visible(true)
            survey_name_et.requestFocus()
            survey_name_et.showKeyboard()
        }
    }

    private fun initViews() {
        survey_name_et.doAfterTextChanged { survey_name_layout.error = null }
        survey_date_et.doAfterTextChanged { survey_date_layout.error = null }

        delete_survey.setOnClickListener { deleteSurvey() }

        done.setOnClickListener {
            if (args.survey != null) {
                viewModel.updateSurvey(surveyFactory())
            } else {
                viewModel.createSurvey(surveyFactory())
            }
        }
        edit.setOnClickListener {
            current_survey.visible(false)
            edit_survey.visible(true)
            survey_name_et.setText(args.survey?.name)
            survey_date_et.setText(args.survey?.date)
        }
    }

    private fun showChangeTypeDialog(types: List<Type>) {
        adapterTypesDialog.setDataSet(types)

        typeIconDialog = MaterialDialog(requireActivity()).show {
            title(R.string.type_caps)
            positiveButton(android.R.string.ok) { this.cancel() }
            customListAdapter(adapterTypesDialog)
        }
    }

    private fun showChangeProfileDialog(marks: List<Mark>) {
        adapterMarksDialog.setDataSet(marks)

        profileMarkDialog = MaterialDialog(requireActivity()).show {
            title(R.string.profile_caps)
            positiveButton(android.R.string.ok) { this.cancel() }
            customListAdapter(adapterMarksDialog)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            survey.observe(viewLifecycleOwner, Observer { survey ->
                setSurvey(survey)
            })
            types.observe(viewLifecycleOwner, Observer { types ->
                if (types.isEmpty()) showAlertCreateSurvey(getString(R.string.type))
                change_survey_type.setOnClickListener { showChangeTypeDialog(types) }
                if (args.survey?.typeId != null) {
                    val type = types.find { it.id == args.survey?.typeId }
                    type?.let { type_icon.setImageResource(it.icon) }
                }
            })
            marks.observe(viewLifecycleOwner, Observer { marks ->
                if (marks.isEmpty()) showAlertCreateSurvey(getString(R.string.profile))
                change_survey_profile.setOnClickListener { showChangeProfileDialog(marks) }
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
            currentProfileMark.observe(viewLifecycleOwner, Observer { mark ->
                profile_mark.setBackgroundColor(mark.color)
            })
            currentType.observe(viewLifecycleOwner, Observer { type ->
                type_icon.setImageResource(type.icon)
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initWorkWithDate() {

        survey_date_et.setOnClickListener {
            val date = Calendar.getInstance()

            val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.set(year, month, dayOfMonth)
                survey_date_et.setText(SimpleDateFormat("dd.MM.yyyy").format(date.time))
            }

            DatePickerDialog(
                requireActivity(),
                listener,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setSurvey(survey: Survey) {
        profile_mark.setBackgroundColor(survey.color)

        survey_name.text = survey.name
        survey_date.text = survey.date
    }

    private fun surveyFactory(): Survey? {
        return if (isFieldsValid()) {
            val id = args.survey?.id ?: DEFAULT_ID_FOR_ENTITY
            val typeId = requireNotNull(currentType.value?.id)
            val profileId = requireNotNull(currentProfileMark.value?.id)
            val color = requireNotNull(currentProfileMark.value?.color)
            val name = survey_name_et.text.toString()
            val date = survey_date_et.text.toString()
            val time = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = time!!
            val month = currentMonth(calendar.time)
            return Survey(
                id = id,
                typeId = typeId,
                profileId = profileId,
                color = color,
                name = name,
                date = date,
                month = month
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
        return if (currentProfileMark.value != null) true
        else {
            requireActivity().toast(R.string.error_empty_profile)
            false
        }
    }

    private fun showAlertCreateSurvey(incompleteDataName: String) {
        MaterialDialog(requireActivity()).show {
            title(text = getString(R.string.survey_incomplete_data, incompleteDataName))
            positiveButton {
                cancel()
            }
        }
    }

    inner class TypesDialogAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    TypesDialogAdapterDelegate(
                        itemClick = {
                            currentType.value = it
                            typeIconDialog?.cancel()
                        }
                    )
                )
        }

        fun setDataSet(types: List<Type>) {
            items.clear()
            items.addAll(types)
            notifyDataSetChanged()
        }
    }

    inner class MarksDialogAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    MarksDialogAdapterDelegate(
                        itemClick = {
                            currentProfileMark.value = it
                            profileMarkDialog?.cancel()
                        }
                    )
                )
        }

        fun setDataSet(marks: List<Mark>) {
            items.clear()
            items.addAll(marks)
            notifyDataSetChanged()
        }
    }

    companion object {
        const val MARGIN_TOP_BOTTOM = 14
        const val MARGIN_START_END = 24
        const val PADDING_START = 24
        const val DEFAULT_ID_FOR_ENTITY = "0"
    }
}
