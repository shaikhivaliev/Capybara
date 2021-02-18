package com.petapp.capybara.presentation.type

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.hideKeyboard
import com.petapp.capybara.extensions.showKeyboard
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_type.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TypeFragment : Fragment(R.layout.fragment_type) {

    private val viewModel: TypeViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val args: TypeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()

        args.type?.id?.apply { viewModel.getType(this) }

        if (args.type?.id == null) {
            icon.setImageResource(DEFAULT_TYPE_IMAGE)
            icon.tag = DEFAULT_TYPE_IMAGE
            delete_surveys_type.isVisible = false
            name_et.requestFocus()
            name_et.showKeyboard()
        }
    }

    private fun initViews() {
        delete_surveys_type.setOnClickListener { deleteType() }

        done.setOnClickListener {
            if (args.type != null) {
                done.hideKeyboard()
                viewModel.updateType(typeBuilder())
            } else {
                done.hideKeyboard()
                viewModel.createType(typeBuilder())
            }
        }
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

    private fun typeBuilder(): Type? {
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
        MaterialDialog(requireActivity()).show {
            if (name.isNotBlank()) {
                title(text = getString(R.string.type_delete_explanation, name))
            } else {
                title(text = getString(R.string.type_delete_explanation_empty))
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

    private fun isNameValid(): Boolean {
        val name = name_et.text.toString()
        return if (name.isNotBlank()) true
        else {
            name_layout.error = requireActivity().getString(R.string.error_empty_name)
            false
        }
    }

    companion object {
        const val DEFAULT_ID_FOR_ENTITY = 0L
        const val DEFAULT_TYPE_IMAGE = R.drawable.ic_default_type_icon
    }
}
