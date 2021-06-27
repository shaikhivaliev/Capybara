package com.petapp.capybara.presentation.profiles

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.petapp.capybara.R
import com.petapp.capybara.common.MarginItemDecoration
import com.petapp.capybara.databinding.FragmentProfilesBinding
import com.petapp.capybara.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val viewBinding by viewBinding(FragmentProfilesBinding::bind)

    private val viewModel: ProfilesViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: ProfilesAdapter = ProfilesAdapter(
        itemClick = { profile -> viewModel.openProfileScreen(profile) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfiles()
        initObservers()
        initRecycler()
        viewBinding.addProfile.setOnClickListener { viewModel.openProfileScreen(null) }
    }

    private fun initRecycler() {
        with(viewBinding.recyclerView) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin_ml),
                    this@ProfilesFragment.adapter.itemCount - 1
                )
            )
        }
    }

    private fun initObservers() {
        with(viewModel) {
            profiles.observe(viewLifecycleOwner, {
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
