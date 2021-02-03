package com.petapp.capybara.presentation.types

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_types.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TypesFragment : Fragment(R.layout.fragment_types) {

    private val viewModel: TypesViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: TypesAdapter by lazy { TypesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@TypesFragment.adapter
            val swipeController = SwipeController(requireContext()) { viewHolder ->
                val type = (viewHolder as TypesAdapterDelegate.ViewHolder).type
                viewModel.openTypeScreen(type)
            }
            val itemTouchHelper = ItemTouchHelper(swipeController)
            itemTouchHelper.attachToRecyclerView(this)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(canvas: Canvas, parent: RecyclerView) {
                    swipeController.onDraw(canvas)
                }
            })
        }
        health_diary.setOnClickListener { viewModel.openHealthDiary() }
        add_type.setOnClickListener { viewModel.openTypeScreen(null) }
    }

    private fun initObservers() {
        with(viewModel) {
            types.observe(viewLifecycleOwner, Observer {
                adapter.setDataSet(it)
            })
            isShowMock.observe(viewLifecycleOwner, Observer { isShow ->
                mock.isVisible = isShow
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
                        itemClick = { viewModel.openSurveysScreen(it.id) }
                    )
                )
        }

        fun setDataSet(types: List<Type>) {
            items.clear()
            items.addAll(types)
            notifyDataSetChanged()
        }
    }
}
