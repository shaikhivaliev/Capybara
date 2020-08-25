package com.petapp.capybara.surveys

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Survey
import com.petapp.capybara.extensions.createChip
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_surveys.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SurveysFragment : Fragment(R.layout.fragment_surveys) {

    private val viewModel: SurveysViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: SurveysAdapter by lazy { SurveysAdapter() }

    private val args: SurveysFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSurveys(args.typeId)
        initObservers()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@SurveysFragment.adapter
        }

        add_survey.setOnClickListener { viewModel.openSurveyScreen(null, true, args.typeId) }
    }

    private fun initObservers() {
        with(viewModel) {
            marks.observe(viewLifecycleOwner, Observer { marks ->
                mark_group.visible(marks.isNotEmpty())
                mark_group.removeAllViews()
                for (mark in marks) {
                    mark_group.addView(createChip(requireContext(), mark))
                }
            })
            surveys.observe(viewLifecycleOwner, Observer {
                adapter.setDataSet(it)
            })
            isShowMock.observe(viewLifecycleOwner, Observer { isShow ->
                mock.visible(isShow)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    inner class SurveysAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    SurveysAdapterDelegate(
                        itemClick = { viewModel.openSurveyScreen(it.id, false, args.typeId) }
                    )
                )
        }

        fun setDataSet(surveys: List<Survey>) {
            items.addAll(surveys)
            notifyDataSetChanged()
        }
    }
}