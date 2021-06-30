package com.petapp.capybara.presentation.types

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.petapp.capybara.R
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

    private val _types = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>> get() = _types

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    private val _isShowMock = MutableLiveData<Boolean>()
    val isShowMock: LiveData<Boolean> get() = _isShowMock

    init {
        getTypes()
    }

    private fun getTypes() {
        typesRepository.getTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isShowMock.value = it.isEmpty()
                    _types.value = it
                    Log.d(TAG, "get types success")
                },
                {
                    _errorMessage.value = R.string.error_get_types
                    Log.d(TAG, "get types error")
                }
            ).connect()
    }

    fun openSurveysScreen(typeId: Long) {
        mainNavigator.openSurveys(typeId)
    }

    fun openHealthDiary() {
        mainNavigator.openHealthDiary(0L)
    }

    companion object {
        private const val TAG = "database_types"
    }
}
