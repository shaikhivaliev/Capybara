package com.petapp.capybara.profiles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val viewModel: ProfilesViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: ProfileAdapter by lazy { ProfileAdapter() }

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
                adapter.setDataSet(it)
            })
            isShowMock.observe(viewLifecycleOwner, Observer { isShow ->
                mock.visible(isShow)
            })
            errorMessage.observe(viewLifecycleOwner, Observer { error ->
                requireActivity().toast(error)
            })
        }
    }

    inner class ProfileAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    ProfilesAdapterDelegate(
                        itemClick = { profile, _ ->
                            viewModel.openProfileScreen(profile)
                        })
                )
        }

        fun setDataSet(profiles: List<Profile>) {
            items.clear()
            items.addAll(profiles)
            notifyDataSetChanged()
        }
    }
}
