package com.petapp.capybara.types

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.TypesRepository
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TypesViewModel(
    private val repository: TypesRepository,
    private val navController: NavController
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
        repository.getTypes()
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

    fun openSurveysScreen(typeId: String) {
        TypesFragmentDirections.toSurveys(typeId).navigateWith(navController)
    }

    fun openTypeScreen(typeId: String?) {
        TypesFragmentDirections.toType(typeId).navigateWith(navController)
    }

    companion object {
        private const val TAG = "database"
    }
}