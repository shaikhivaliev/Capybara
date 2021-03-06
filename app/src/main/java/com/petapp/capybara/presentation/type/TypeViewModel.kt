package com.petapp.capybara.presentation.type

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.TypesRepository
import com.petapp.capybara.data.model.Type
import com.petapp.capybara.extensions.navigateWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TypeViewModel(
    private val repository: TypesRepository,
    private val navController: NavController
) : BaseViewModel() {

    private val _type = MutableLiveData<Type>()
    val type: LiveData<Type> get() = _type

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage: LiveData<Int> get() = _errorMessage

    fun getType(typeId: Long) {
        repository.getType(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _type.value = it
                Log.d(TAG, "get type ${it.id} success")
            },
                {
                    _errorMessage.value = R.string.error_get_type
                    Log.d(TAG, "get type error")
                }
            ).connect()
    }

    fun createType(type: Type?) {
        if (type != null) {
            repository.createType(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    openTypesScreen()
                    Log.d(TAG, "create survey success")
                }, {
                    _errorMessage.value = R.string.error_create_type
                    Log.d(TAG, "create survey error")
                })
                .connect()
        }
    }

    fun updateType(type: Type?) {
        if (type != null) {
            repository.updateType(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        openTypesScreen()
                        Log.d(TAG, "update type ${type.id} success")
                    },
                    {
                        _errorMessage.value = R.string.error_update_type
                        Log.d(TAG, "update type ${type.id} error")
                    }
                ).connect()
        }
    }

    fun deleteType(typeId: Long) {
        repository.deleteType(typeId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    openTypesScreen()
                    Log.d(TAG, "delete type $typeId success")
                },
                {
                    _errorMessage.value = R.string.error_delete_type
                    Log.d(TAG, "delete type error")
                }
            ).connect()
    }

    fun openTypesScreen() {
        TypeFragmentDirections.toTypes().navigateWith(navController)
    }

    companion object {
        private const val TAG = "database_type"
    }
}
