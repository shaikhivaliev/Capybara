package com.petapp.capybara.calendar.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.petapp.capybara.R
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private val viewModel: CalendarViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMarks()
        initObservers()
    }

    private fun initObservers() {
        viewModel.marks.observe(viewLifecycleOwner, Observer { marks ->
            mark_group.visible(marks.isNotEmpty())
            mark_group.removeAllViews()
            for (mark in marks) {
                mark_group.addView(createChip(requireContext(), mark))
            }
        })
    }
}