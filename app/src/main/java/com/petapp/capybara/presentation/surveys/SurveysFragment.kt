package com.petapp.capybara.presentation.surveys

import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.chip.Chip
import com.petapp.capybara.R
import com.petapp.capybara.core.list.MarginItemDecoration
import com.petapp.capybara.core.navigation.LongNavDto
import com.petapp.capybara.core.navigation.SurveyNavDto
import com.petapp.capybara.core.navigation.navDto
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.databinding.FragmentSurveysBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.main.MainActivity
import javax.inject.Inject

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    private val viewBinding by viewBinding(FragmentSurveysBinding::bind)

    private val args: LongNavDto by navDto()

    @Inject
    lateinit var vmFactory: SurveysVmFactory

    private val vm: SurveysVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val adapter: SurveysAdapter = SurveysAdapter(
        itemClick = { vm.openSurveyScreen(it) }
    )

    private val chipIdToProfileId = mutableMapOf<Int, Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecycler()
        viewBinding.addSurvey.setOnClickListener { vm.openSurveyScreen(null) }
        viewBinding.marksGroup.setOnCheckedChangeListener { _, checkedId ->
            vm.profileId.value = chipIdToProfileId[checkedId]
            args.value?.let { vm.getSurveys(it) }
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
        with(vm) {
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
                positiveButton { vm.openProfileScreen() }
            }
    }

    companion object {
        const val CHIP_PADDING = 56F
    }
}
