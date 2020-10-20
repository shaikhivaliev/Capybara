package com.petapp.capybara.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import androidx.lifecycle.Observer
import com.petapp.capybara.view.CalendarView
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel {
        parametersOf(findNavController())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_survey.showAdd()

        initViews(view)
        initObservers()
        val month = SimpleDateFormat("MMMM", Locale.ENGLISH).format(Calendar.getInstance().time)
        viewModel.getSurveysByMonth(month)

        add_survey.setOnClickListener { viewModel.openSurveyScreen(null) }
    }

    private fun initViews(view: View) {
        marks_group.setOnCheckedChangeListener { _, checkedId ->
            val marksButton = view.findViewById<Chip>(checkedId)
        }
        calendar.onChangeMonthListener = object : CalendarView.OnChangeMonthListener {
            override fun onChangeMonth(month: String) {
                return viewModel.getSurveysByMonth(month)
            }
        }
    }

    private fun initObservers() {
        with(viewModel) {
            marks.observe(viewLifecycleOwner, Observer { marks ->
                for (mark in marks) {
                    marks_group.addView(createChip(requireContext(), mark, CHIP_PADDING))
                }
            })
            surveys.observe(viewLifecycleOwner, Observer {
                calendar.setupCalendar(it)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    companion object {
        const val CHIP_PADDING = 56F
    }
}
