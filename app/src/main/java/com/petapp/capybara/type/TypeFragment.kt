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
        initViews()
        initObservers()

        args.type?.id?.apply { viewModel.getType(this) }

        delete_type.setOnClickListener { deleteType() }

        done.setOnClickListener {
            if (args.type != null) {
                viewModel.updateType(typeFactory())
            } else {
                viewModel.createType(typeFactory())
            }
        }
    }

    private fun initViews(){
        with(recycler) {
            this.layoutManager = GridLayoutManager(context, 3)
            adapter = this@TypeFragment.adapter
        }
        val iconResList = arrayListOf(
            R.drawable.ic_blood,
            R.drawable.ic_digistion,
            R.drawable.ic_heart,
            R.drawable.ic_reproductive_system,
            R.drawable.ic_vaccination,
            R.drawable.ic_vision
        )
        adapter.setDataSet(iconResList)
        icon.setImageResource(DEFAULT_TYPE_IMAGE)
        icon.tag = DEFAULT_TYPE_IMAGE
    }

    private fun initObservers() {
        with(viewModel) {
            type.observe(viewLifecycleOwner, Observer { type ->
                setType(type)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    private fun setType(type: Type) {
        name_et.setText(type.name)
        icon.setImageResource(type.icon)
        icon.tag = type.icon
    }

    private fun typeFactory(): Type? {
        return if (isNameValid()) {
            val id = args.type?.id ?: DEFAULT_ID_FOR_ENTITY
            val name = name_et.text.toString()
            val icon = icon.tag as Int
            Type(id = id, name = name, icon = icon)
        } else {
            null
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
                    if (args.type?.id != null) {
                         viewModel.deleteType(args.type?.id!!)
                    } else {
                        viewModel.openTypesScreen()
                    }
                    cancel()
                }
                negativeButton { cancel() }
            }
        }
    }

    private fun isNameValid(): Boolean {
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = requireActivity().getString(R.string.error_empty_name)
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

    companion object {
        private const val DEFAULT_ID_FOR_ENTITY = "0"
        private const val DEFAULT_TYPE_IMAGE = R.drawable.ic_digistion
    }
}