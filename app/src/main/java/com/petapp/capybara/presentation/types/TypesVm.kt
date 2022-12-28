package com.petapp.capybara.presentation.types

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
import com.petapp.capybara.core.DataState
import com.petapp.capybara.core.navigation.IMainNavigator
import com.petapp.capybara.core.viewmodel.BaseViewModel
import com.petapp.capybara.core.viewmodel.SavedStateVmAssistedFactory
import com.petapp.capybara.data.ITypesRepository
import com.petapp.capybara.data.model.Type
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
) : BaseViewModel() {

    private val _typesState = MutableLiveData<DataState<List<Type>>>()
    val typesState: LiveData<DataState<List<Type>>> get() = _typesState

    init {
        getTypes()
    }

    fun getTypes() {
        typesRepository.getTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) {
                        _typesState.value = DataState.EMPTY
                    } else {
                        _typesState.value = DataState.DATA(it)
                    }
                },
                {
                    _typesState.value = DataState.ERROR(it)
                }
            ).connect()
    }

    fun openSurveysScreen(typeId: Long) {
        mainNavigator.openSurveys(typeId)
    }

    fun openHealthDiary() {
        mainNavigator.openHealthDiary(0L)
    }
}
