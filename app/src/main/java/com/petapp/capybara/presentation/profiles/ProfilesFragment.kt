package com.petapp.capybara.presentation.profiles

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.*
import javax.inject.Inject

class ProfilesFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ProfilesVmFactory

    private val vm: ProfilesVm by stateViewModel(
        vmFactoryProducer = { vmFactory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    ProfilesScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun ProfilesScreen() {
        val profileState by vm.profilesState.observeAsState()
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.openProfileScreen(null)
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            },
            content = {
                when (val state = profileState) {
                    DataState.EMPTY -> EmptyData(stringResource(R.string.profile_mock))
                    is DataState.ERROR -> ShowError()
                    is DataState.DATA -> ShowProfiles(state.data)
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowProfiles(profiles: List<Profile>) {
        StandardColumn {
            items(profiles) { item ->
                CircleIconTitleDescItem(
                    onItemClick = { vm.openProfileScreen(item) },
                    item = item.toUiData(),
                    modifier = modifierCircleIcon76dp()
                )
            }
        }
    }

    @Composable
    private fun ShowError() {
        // todo
    }
}
