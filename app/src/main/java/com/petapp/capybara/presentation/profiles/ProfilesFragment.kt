package com.petapp.capybara.presentation.profiles

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.petapp.capybara.R
import com.petapp.capybara.extensions.toast
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

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

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
        }
        add_profile.setOnClickListener { viewModel.openProfileScreen(null) }
    }

    private fun initObservers() {
        with(viewModel) {
            profiles.observe(viewLifecycleOwner, Observer {
                adapter.items = it
            })
            isShowMock.observe(viewLifecycleOwner, Observer { isShow ->
                mock.isVisible = isShow
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }
}
