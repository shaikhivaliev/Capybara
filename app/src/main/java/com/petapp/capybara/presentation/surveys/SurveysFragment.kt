package com.petapp.capybara.presentation.surveys

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.common.MarginItemDecoration
import com.petapp.capybara.databinding.FragmentSurveysBinding
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    private val viewBinding by viewBinding(FragmentSurveysBinding::bind)

    private val args: SurveysFragmentArgs by navArgs()

    private val viewModel: SurveysViewModel by viewModel {
        parametersOf(findNavController(), args.typeId)
    }

    private val adapter: SurveysAdapter = SurveysAdapter(
        itemClick = { viewModel.openSurveyScreen(it) }
    )

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecycler()
        viewBinding.addSurvey.setOnClickListener { viewModel.openSurveyScreen(null) }
        viewBinding.marksGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.profileId.value = chipIdToProfileId[checkedId]
            viewModel.getSurveys()
        }
    }

    private fun initRecycler() {
        with(viewBinding.recyclerView) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@SurveysFragment.adapter
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin_ml),
                    this@SurveysFragment.adapter.itemCount - 1
                )
            )
            itemAnimator = null
        }
    }

    private fun initObservers() {
        with(viewModel) {
            profiles.observe(viewLifecycleOwner, { profiles ->
                if (profiles.isEmpty()) {
                    showAlertEmptyProfiles()
                } else {
                    for (profile in profiles) {
                        val chip = createChip(requireContext(), profile, CHIP_PADDING)
                        viewBinding.marksGroup.addView(chip)
                        chipIdToProfileId[chip.id] = profile.id
                    }
                    (viewBinding.marksGroup[0] as? Chip)?.isChecked = true
                }
            })

            surveys.observe(viewLifecycleOwner, { surveys ->
                viewBinding.mock.isVisible = surveys.isNullOrEmpty()
                adapter.items = surveys
            })

            errorMessage.observe(viewLifecycleOwner, { error ->
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
