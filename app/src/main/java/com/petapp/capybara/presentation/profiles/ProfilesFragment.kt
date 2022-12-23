package com.petapp.capybara.presentation.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.Profile
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.ui.textMedium
import com.petapp.capybara.ui.textSmall
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

    @Composable
    private fun ProfilesScreen() {
        val profileItems: List<Profile> = vm.profiles.observeAsState(initial = emptyList()).value
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
            content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        items(profileItems) { item ->
                            ProfileItem(item)
                        }
                    })
            }
        )
    }


    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun ProfileItem(profile: Profile) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                vm.openProfileScreen(profile)
            },
            verticalAlignment = Alignment.CenterVertically,
            content = {
                GlideImage(
                    model = profile.photo,
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(
                            color = Color(profile.color),
                            width = 2.dp,
                            shape = RoundedCornerShape(percent = 50)
                        )
                ) {
                    it
                        .error(R.drawable.ic_user_42dp)
                }
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = profile.name,
                        style = textMedium()
                    )
                    Text(
                        text = profile.surveys.size.toString(),
                        style = textSmall()
                    )
                }
            })
//        photo.transitionName = item.name
    }

    private fun initObservers() {
//        with(vm) {
//            isShowMock.observe(viewLifecycleOwner) { isShow ->
//                viewBinding.mock.isVisible = isShow
//            }
//            errorMessage.observe(viewLifecycleOwner) { error ->
//                requireActivity().toast(error)
//            }
//        }
    }
}
