package com.petapp.capybara.profiles.presentation.profiles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.profiles.domain.dto.Profile
import com.petapp.capybara.profiles.presentation.profile.ProfileFragment
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val viewModel: ProfilesViewModel by viewModel()

    private val adapter: ProfileAdapter by lazy { ProfileAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfiles()
        initObservers()
        add_profile.showAdd()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
        }
        add_profile.setOnClickListener {
            findNavController().navigate(R.id.profile, ProfileFragment.createBundle(null, true, null))
        }
    }

    private fun initObservers() {
        viewModel.profiles.observe(viewLifecycleOwner, Observer {
            adapter.setDataSet(it)
        })
        viewModel.isShowMock.observe(viewLifecycleOwner, Observer { isShow ->
            mock.visible(isShow)
        })
    }


    inner class ProfileAdapter : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager
                .addDelegate(
                    ProfilesAdapterDelegate(
                        itemClick = { profile, view ->
                            findNavController().navigate(
                                R.id.profile,
                                ProfileFragment.createBundle(profile.id, false, profile.name),
                                null,
                                FragmentNavigatorExtras(view to profile.name)
                            )
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