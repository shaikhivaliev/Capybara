package com.petapp.profiles.presentation.profiles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.profiles.R
import com.petapp.profiles.domain.Profile
import com.petapp.profiles.visible
import kotlinx.android.synthetic.main.fragment_profiles.*
import javax.inject.Inject

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val adapter: ProfileAdapter by lazy { ProfileAdapter() }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    lateinit var viewModel: ProfilesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.inject(this)
        activity?.let { viewModel = ViewModelProvider(it, factory).get(ProfilesViewModel::class.java) }

        viewModel.getProfiles()
        initObservers()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
        }
        add_profile.setOnClickListener {
            viewModel.navigateTo(Screens.Profile(null, true))
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
                        itemClick = { viewModel.navigateTo(Screens.Profile(it.id, false)) })
                )
        }

        fun setDataSet(profiles: List<Profile>) {
            items.clear()
            items.addAll(profiles)
            notifyDataSetChanged()
        }
    }

}