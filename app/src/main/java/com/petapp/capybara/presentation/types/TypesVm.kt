package com.petapp.capybara.presentation.types

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.ITypesRepository
import kotlinx.coroutines.launch

class TypesVmFactory(
    private val mainNavigator: IMainNavigator,
    private val typesRepository: ITypesRepository,
    private val store: TypesStore
) : SavedStateVmAssistedFactory<TypesVm> {
    override fun create(handle: SavedStateHandle) =
        TypesVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            typesRepository = typesRepository,
            store = store
        )
}

class TypesVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val typesRepository: ITypesRepository,
    val store: TypesStore
) : ViewModel() {

    init {
        store.launch(viewModelScope)
        getTypes()
    }

    fun collectStore() = store.state

    private fun getTypes() {
        viewModelScope.launch {
            runCatching {
                typesRepository.getTypes()
            }
                .onSuccess {
                    if (it.isEmpty()) {
                        store.dispatch(TypesEvent.TypesEmpty)
                    } else {
                        store.dispatch(TypesEvent.TypesLoaded(it))
                    }
                }
                .onFailure {
                    store.dispatch(TypesEvent.TypesError(it))
                }
        }
    }

    fun openSurveysScreen(typeId: Long) {
        mainNavigator.openSurveys(typeId)
    }

    fun openHealthDiary() {
        mainNavigator.openHealthDiary(0L)
    }
}
