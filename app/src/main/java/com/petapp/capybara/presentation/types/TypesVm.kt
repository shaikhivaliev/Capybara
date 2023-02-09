package com.petapp.capybara.presentation.types

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.repository.TypesRepository
import com.petapp.capybara.navigation.MainNavigatorImpl
import com.petapp.capybara.viewmodel.SavedStateVmAssistedFactory
import kotlinx.coroutines.launch

class TypesVmFactory(
    private val mainNavigator: MainNavigatorImpl,
    private val typesRepository: TypesRepository,
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
    private val mainNavigator: MainNavigatorImpl,
    private val typesRepository: TypesRepository,
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
