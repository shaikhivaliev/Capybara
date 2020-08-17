package com.petapp.capybara.type

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.UniqueId
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.argument
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_type.*
import org.koin.android.ext.android.inject

class TypeFragment : Fragment(R.layout.fragment_type) {

    companion object {
        private const val TYPE_ID = "TYPE_ID"
        private const val IS_NEW_TYPE = "IS_NEW_TYPE"

        fun create(typeId: String?, isNew: Boolean): Bundle {
            return Bundle().apply {
                putString(TYPE_ID, typeId)
                putBoolean(IS_NEW_TYPE, isNew)
            }
        }
    }

    private val viewModel: TypeViewModel by inject()

    private val adapter: TypeIconAdapter by lazy { (TypeIconAdapter()) }

    private val typeId by argument(TYPE_ID, "")
    private val isNewType by argument(IS_NEW_TYPE, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.showDone()
        if (!isNewType) viewModel.getType(typeId)
        initObservers()
        delete_type.setOnClickListener { deleteType() }
        done.setOnClickListener { if (isNewType) createType() else updateType() }
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
            val type =
                Type(id = typeId, name = name, icon = icon.tag as Int)
            viewModel.updateType(type)
        }
    }

    private fun deleteType() {
        val name = name_et.text.toString()
        activity?.let {
            MaterialDialog(it).show {
                if (name.isNotBlank()) {
                    title(text = getString(R.string.cancel_explanation, name))
                } else {
                    title(text = getString(R.string.cancel_explanation_empty))
                }
                positiveButton {
                    if (!isNewType) viewModel.deleteType(typeId) else navigateBack()
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
                if (isDone) navigateBack()
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
        if (!isNewType) return true
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = "Пустое имя"
            false
        }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.tab_types)
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