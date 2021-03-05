package com.petapp.capybara.presentation.surveys

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.calendar.SurveysAdapter
import kotlinx.android.synthetic.main.fragment_surveys.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    private val args: SurveysFragmentArgs by navArgs()

    private val viewModel: SurveysViewModel by viewModel {
        parametersOf(findNavController(), args.typeId)
    }

    private val adapter: SurveysAdapter =
        SurveysAdapter(
            itemClick = { viewModel.openSurveyScreen(it) },
            addNewSurvey = {}
        )

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
        add_survey.setOnClickListener { viewModel.openSurveyScreen(null) }
    }

    private fun initViews(view: View) {
        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@SurveysFragment.adapter
        }

        marks_group.setOnCheckedChangeListener { _, checkedId ->
            viewModel.profileId.value = chipIdToProfileId[checkedId]
            viewModel.getSurveys()
        }
    }

    private fun initObservers() {
        with(viewModel) {
            profiles.observe(viewLifecycleOwner, Observer { profiles ->
                if (profiles.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (profile in profiles) {
                        val chip = createChip(requireContext(), profile, CHIP_PADDING)
                        marks_group.addView(chip)
                        chipIdToProfileId[chip.id] = profile.id
                    }
                    (marks_group[0] as? Chip)?.isChecked = true
                }
            })

            surveys.observe(viewLifecycleOwner, Observer { surveys ->
                mock.isVisible = surveys.isNullOrEmpty()
                adapter.items = surveys
            })

            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun showAlertEmptyProfiles() {
        MaterialDialog(requireActivity())
            .cancelable(false)
            .show {
                title(text = getString(R.string.survey_incomplete_data))
                positiveButton { viewModel.openProfileScreen() }
            }
    }

    companion object {
        const val CHIP_PADDING = 56F
    }
}
