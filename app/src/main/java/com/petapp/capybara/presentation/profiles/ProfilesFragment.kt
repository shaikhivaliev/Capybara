package com.petapp.capybara.presentation.profiles

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.data.model.Profile
import com.petapp.capybara.core.mvi.DataState
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.*
import com.petapp.capybara.ui.list.BaseLazyColumn
import com.petapp.capybara.ui.list.IconTitleDescItem
import com.petapp.capybara.ui.state.Empty
import com.petapp.capybara.ui.state.Error
import com.petapp.capybara.viewmodel.stateViewModel
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
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val profileState by vm.profilesState.collectAsState()
        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.openNewProfile()
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            },
            content = {
                when (val state = profileState) {
                    DataState.EMPTY -> Empty(stringResource(R.string.profile_mock))
                    is DataState.ERROR -> Error()
                    is DataState.DATA -> ShowProfiles(state.data)
                    else -> { // nothing
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowProfiles(profiles: List<Profile>) {
        BaseLazyColumn {
            items(profiles) { item ->
                IconTitleDescItem(
                    onClick = { vm.openProfileScreen(item) },
                    item = item.toUiData(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
