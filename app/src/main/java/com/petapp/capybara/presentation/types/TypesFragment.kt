package com.petapp.capybara.presentation.types

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.viewmodel.stateViewModel
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.di.features.FeaturesComponentHolder
import com.petapp.capybara.presentation.main.MainActivity
import com.petapp.capybara.presentation.toUiData
import com.petapp.capybara.ui.*
import javax.inject.Inject

class TypesFragment : Fragment() {

    @Inject
    lateinit var vmFactory: TypesVmFactory

    private val vm: TypesVm by stateViewModel(vmFactoryProducer = { vmFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FeaturesComponentHolder.getComponent(requireActivity() as MainActivity)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    TypesScreen()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun TypesScreen() {
        val scaffoldState: ScaffoldState = rememberScaffoldState()
        val typeState by vm.typesState.observeAsState()
        Scaffold(
            scaffoldState = scaffoldState,
            content = {
                Column {
                    Card(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp), elevation = 4.dp
                    ) {
                        Row(modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                vm.openHealthDiary()
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            content = {
                                Image(
                                    painter = painterResource(R.drawable.ic_healt_diary),
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside
                                )
                                Text(
                                    text = stringResource(R.string.health_diary_health_diary),
                                    style = textMedium(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            })
                    }
                    when (val state = typeState) {
                        DataState.EMPTY -> EmptyData(stringResource(R.string.profile_mock))
                        is DataState.ERROR -> ShowError(
                            scaffoldState = scaffoldState,
                            errorMessage = stringResource(R.string.error_explanation),
                            action = { vm.getTypes() }
                        )
                        is DataState.DATA -> ShowTypes(state.data)
                        else -> { // nothing
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun ShowTypes(types: List<Type>) {
        StandardColumn {
            items(types) { item ->
                CircleIconTitleDescItem(
                    onItemClick = { vm.openSurveysScreen(item.id) },
                    item = item.toUiData(),
                    modifier = modifierCircleIcon76dp()
                )
            }
        }
    }
}
