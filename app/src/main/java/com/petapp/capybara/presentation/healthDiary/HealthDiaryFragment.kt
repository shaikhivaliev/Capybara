package com.petapp.capybara.presentation.healthDiary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.petapp.capybara.R
import com.petapp.capybara.adapter.StandardListAdapter
import com.petapp.capybara.adapter.emptySurveyHealthDiaryDelegate
import com.petapp.capybara.adapter.itemHealthDiaryDelegate
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.toPresentationModel
import kotlinx.android.synthetic.main.fragment_health_diary.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HealthDiaryFragment : Fragment(R.layout.fragment_health_diary) {

    private val viewModel: HealthDiaryViewModel by viewModel()

    private val adapter by lazy {
        StandardListAdapter(
            itemHealthDiaryDelegate { viewModel.handleStepClick(it) },
            emptySurveyHealthDiaryDelegate()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@HealthDiaryFragment.adapter
        }
    }

    private fun initObservers() {
        with(viewModel) {
            healthDiaryItems.observe(viewLifecycleOwner, Observer {
                adapter.items = it.toPresentationModel()
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }
}
