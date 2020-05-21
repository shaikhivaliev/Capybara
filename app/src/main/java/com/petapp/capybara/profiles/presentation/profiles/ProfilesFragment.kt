package com.petapp.capybara.profiles.presentation.profiles

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.petapp.capybara.R
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.navigation.Screens
import com.petapp.capybara.profiles.domain.Profile
import kotlinx.android.synthetic.main.fragment_profiles.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.atomic.AtomicInteger


class ProfilesFragment : Fragment(R.layout.fragment_profiles) {

    private val viewModel: ProfilesViewModel by viewModel()

/*
    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this.activity, supportFragmentManager, R.id.container) {
            override fun setupFragmentTransaction(
                command: Command,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                fragmentTransaction.setCustomAnimations()
            }
        }
    }
*/

    private val adapter: ProfileAdapter by lazy { ProfileAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfiles()
        initObservers()

        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@ProfilesFragment.adapter
        }
        add_profile.setOnClickListener {
            val profile = Profile(id = UniqueId.id.toString(), color = android.R.color.white, name = "", photo = null)
            adapter.setData(profile)
            viewModel.navigateTo(Screens.Profile(profile.id))
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
                        itemClick = { viewModel.navigateTo(Screens.Profile(it.id)) })
                )
        }

        fun setData(profile: Profile) {
            items.add(profile)
            notifyDataSetChanged()
            recycler_view.smoothScrollToPosition(itemCount)
        }

        fun setDataSet(profiles: List<Profile>) {
            items.addAll(profiles)
            notifyDataSetChanged()
            recycler_view.smoothScrollToPosition(itemCount)
        }
    }

    class UniqueId {
        companion object {
            private val c = AtomicInteger(0)
            val id: Int
                get() = c.incrementAndGet()
        }
    }

}