package com.petapp.capybara.presentation.types

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.petapp.capybara.R
import com.petapp.capybara.core.list.MarginItemDecoration
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.databinding.FragmentTypesBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.main.MainActivity
import javax.inject.Inject

class TypesFragment : Fragment(R.layout.fragment_types) {

    private val viewBinding by viewBinding(FragmentTypesBinding::bind)

    @Inject
    lateinit var vmFactory: TypesVmFactory

    private val vm: TypesVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val adapter: TypesAdapter = TypesAdapter(
        itemClick = { vm.openSurveysScreen(it.id) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecycler()
        viewBinding.healthDiary.root.setOnClickListener { vm.openHealthDiary() }
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
        with(vm) {
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
