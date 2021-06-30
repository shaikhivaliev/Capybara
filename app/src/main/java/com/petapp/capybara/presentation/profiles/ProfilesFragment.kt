package com.petapp.capybara.presentation.profiles

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.petapp.capybara.R
import com.petapp.capybara.core.list.MarginItemDecoration
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.databinding.FragmentProfilesBinding
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.extensions.toast
import com.petapp.capybara.presentation.main.MainActivity
import javax.inject.Inject

class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val viewBinding by viewBinding(FragmentProfilesBinding::bind)

    @Inject
    lateinit var vmFactory: ProfilesVmFactory

    private val vm: ProfilesVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    private val adapter: ProfilesAdapter = ProfilesAdapter(
        itemClick = { profile -> vm.openProfileScreen(profile) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initRecycler()
        viewBinding.addProfile.setOnClickListener { vm.openProfileScreen(null) }
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
        with(vm) {
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
