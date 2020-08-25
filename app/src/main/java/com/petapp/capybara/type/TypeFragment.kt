package com.petapp.capybara.type

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.UniqueId
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_type.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TypeFragment : Fragment(R.layout.fragment_type) {

    private val viewModel: TypeViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: TypeIconAdapter by lazy { (TypeIconAdapter()) }

    private val args: TypeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.showDone()
        args.typeId?.apply { viewModel.getType(this) }
        initObservers()
        delete_type.setOnClickListener { deleteType() }
        done.setOnClickListener { if (args.isNewType) createType() else updateType() }
        with(recycler) {
            this.layoutManager = GridLayoutManager(context, 3)
            adapter = this@TypeFragment.adapter
        }
        val iconResList = arrayListOf<Int>(
            R.drawable.ic_blood,
            R.drawable.ic_digistion,
            R.drawable.ic_heart,
            R.drawable.ic_reproductive_system,
            R.drawable.ic_vaccination,
            R.drawable.ic_vision
        )
        adapter.setDataSet(iconResList)
    }

    private fun createType() {
        if (isNameValid()) {
            val id = UniqueId.id.toString()
            val name = name_et.text.toString()
            val type = Type(id = id, name = name, icon = R.drawable.ic_vaccination)
            viewModel.createType(type)
        }
    }

    private fun updateType() {
        if (isNameValid()) {
            val name = name_et.text.toString()
            args.typeId?.apply {
                val type = Type(id = this, name = name, icon = icon.tag as Int)
                viewModel.updateType(type)
            }
        }
    }

    private fun deleteType() {
        val name = name_et.text.toString()
        activity?.let {
            MaterialDialog(it).show {
                if (name.isNotBlank()) {
                    title(text = getString(R.string.profile_delete_explanation, name))
                } else {
                    title(text = getString(R.string.profile_delete_explanation_empty))
                }
                positiveButton {
                    if (!args.isNewType) args.typeId?.apply { viewModel.deleteType(this) } else viewModel.back()
                    cancel()
                }
                negativeButton { cancel() }
            }
        }
    }

    private fun initObservers() {
        with(viewModel) {
            type.observe(viewLifecycleOwner, Observer { type ->
                setType(type)
            })
            isChangeDone.observe(viewLifecycleOwner, Observer { isDone ->
                if (isDone) viewModel.back()
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setType(type: Type) {
        name_et.setText(type.name)
        icon.setImageResource(type.icon)
    }

    private fun isNameValid(): Boolean {
        if (!args.isNewType) return true
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    inner class TypeIconAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    TypeIconAdapterDelegate(
                        iconClick = {
                            icon.setImageResource(it)
                            icon.tag = it
                        }
                    )
                )
        }

        fun setDataSet(iconRes: List<Int>) {
            items.clear()
            items.addAll(iconRes)
            notifyDataSetChanged()
        }
    }
}