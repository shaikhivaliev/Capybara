package com.petapp.capybara.types

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.surveys.SurveysFragment
import com.petapp.capybara.type.TypeFragment
import kotlinx.android.synthetic.main.fragment_types.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypesFragment : Fragment(R.layout.fragment_types) {

    private val viewModel: TypesViewModel by viewModel()

    private val adapter: TypesAdapter by lazy { TypesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_type.showAdd()
        viewModel.getTypes()
        initObservers()

        with(recycler_view) {
            this.layoutManager = GridLayoutManager(context, 2)
            adapter = this@TypesFragment.adapter
        }

        add_type.setOnClickListener {
            navigateToType(null, true)
        }
    }

    private fun initObservers() {
        with(viewModel) {
            types.observe(viewLifecycleOwner, Observer {
                adapter.setDataSet(it)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    inner class TypesAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    TypesAdapterDelegate(
                        itemClick = { navigateToSurveys(it.id) },
                        editClick = { navigateToType(it.id, false) }
                    )
                )
        }

        fun setDataSet(types: List<Type>) {
            items.clear()
            items.addAll(types)
            notifyDataSetChanged()
        }
    }

    private fun navigateToSurveys(typeId: String) {
        findNavController().navigate(R.id.surveys, SurveysFragment.createBundle(typeId))
    }

    private fun navigateToType(typeId: String?, isNewType: Boolean) {
        findNavController().navigate(R.id.type, TypeFragment.create(typeId, isNewType))
    }
}