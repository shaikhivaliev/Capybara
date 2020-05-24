package com.petapp.capybara.calendar.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.calendar.domain.Mark
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    companion object {
        const val CHIP_PADDING = 56F
    }

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
                mark_group.addView(createChip(mark))
            }
        })
    }

    private fun createChip(mark: Mark): Chip {
        val chip = Chip(this.activity)
        chip.apply {
            chipEndPadding = CHIP_PADDING
            chipStartPadding = CHIP_PADDING
            text = mark.name
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            isCheckable = true
            checkedIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done_black)
            chipBackgroundColor = getColorStateList(requireContext(), mark.color)

        }
        return chip
    }
}