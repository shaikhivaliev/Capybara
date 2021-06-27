package com.petapp.capybara.presentation.types

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.petapp.capybara.R
import com.petapp.capybara.common.MarginItemDecoration
import com.petapp.capybara.databinding.FragmentTypesBinding
import com.petapp.capybara.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TypesFragment : Fragment(R.layout.fragment_types) {

    private val viewBinding by viewBinding(FragmentTypesBinding::bind)

    private val viewModel: TypesViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: TypesAdapter = TypesAdapter(
        itemClick = { viewModel.openSurveysScreen(it.id) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecycler()
        viewBinding.healthDiary.root.setOnClickListener { viewModel.openHealthDiary() }
    }

    private fun initRecycler() {
        with(viewBinding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TypesFragment.adapter
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin_ml),
                    this@TypesFragment.adapter.itemCount - 1
                )
            )
        }
    }

    private fun initObservers() {
        with(viewModel) {
            types.observe(viewLifecycleOwner, {
                adapter.items = it
            })
            isShowMock.observe(viewLifecycleOwner, { isShow ->
                viewBinding.mock.isVisible = isShow
            })
            errorMessage.observe(viewLifecycleOwner, { error ->
                requireActivity().toast(error)
            })
        }
    }
}
