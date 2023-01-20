package com.petapp.capybara.presentation.types

import androidx.lifecycle.*
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.ITypesRepository
import com.petapp.capybara.data.model.Type
import kotlinx.coroutines.launch

class TypesVmFactory(
    private val mainNavigator: IMainNavigator,
    private val typesRepository: ITypesRepository
) : SavedStateVmAssistedFactory<TypesVm> {
    override fun create(handle: SavedStateHandle) =
        TypesVm(
            savedStateHandle = handle,
            mainNavigator = mainNavigator,
            typesRepository = typesRepository
        )
}

class TypesVm(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigator: IMainNavigator,
    private val typesRepository: ITypesRepository
) : ViewModel() {

    private val _typesState = MutableLiveData<DataState<List<Type>>>()
    val typesState: LiveData<DataState<List<Type>>> get() = _typesState

    init {
        getTypes()
    }

    private fun getTypes() {
        viewModelScope.launch {
            runCatching {
                typesRepository.getTypes()
            }
                .onSuccess {
                    if (it.isEmpty()) {
                        _typesState.value = DataState.EMPTY
                    } else {
                        _typesState.value = DataState.DATA(it)
                    }
                }
                .onFailure {
                    _typesState.value = DataState.ERROR(it)
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
