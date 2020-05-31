package com.petapp.capybara.surveys.presentation.surveys

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.extensions.argument
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.surveys.domain.dto.Survey
import com.petapp.capybara.surveys.presentation.survey.SurveyFragment
import kotlinx.android.synthetic.main.fragment_surveys.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    companion object {
        private const val TYPE_ID = "TYPE_ID"

        fun createBundle(typeId: String?): Bundle {
            return Bundle().apply {
                putString(TYPE_ID, typeId)
            }
        }
    }

    private val typeId by argument(TYPE_ID, "")

    private val viewModel: SurveysViewModel by viewModel()

    private val adapter: SurveysAdapter by lazy { SurveysAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSurveys(typeId)
        initObservers()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@SurveysFragment.adapter
        }

        add_survey.setOnClickListener {
            navigateToSurvey(null, typeId, true)
        }
    }

    private fun initObservers() {
        viewModel.marks.observe(viewLifecycleOwner, Observer { marks ->
            mark_group.visible(marks.isNotEmpty())
            mark_group.removeAllViews()
            for (mark in marks) {
                mark_group.addView(createChip(requireContext(), mark))
            }
        })
        viewModel.surveys.observe(viewLifecycleOwner, Observer {
            adapter.setDataSet(it)
        })
        viewModel.isShowMock.observe(viewLifecycleOwner, Observer { isShow ->
            mock.visible(isShow)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            requireActivity().toast(message)
        })
    }

    inner class SurveysAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    SurveysAdapterDelegate(
                        itemClick = { navigateToSurvey(it.id, typeId, false) }
                    )
                )
        }

        fun setDataSet(surveys: List<Survey>) {
            items.addAll(surveys)
            notifyDataSetChanged()
        }
    }

    private fun navigateToSurvey(surveyId: String?, typeId: String, isNewSurvey: Boolean) {
        findNavController().navigate(R.id.survey, SurveyFragment.create(surveyId, typeId, isNewSurvey))
    }

}