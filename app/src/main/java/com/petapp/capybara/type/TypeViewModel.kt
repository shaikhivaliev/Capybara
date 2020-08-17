package com.petapp.capybara.type

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.BaseViewModel
import com.petapp.capybara.R
import com.petapp.capybara.data.TypesRepository
import com.petapp.capybara.data.model.Type
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TypeViewModel(
    private val repository: TypesRepository
) : BaseViewModel() {

    private val _type = MutableLiveData<Type>()
    val type : LiveData<Type> get() = _type

    private val _isChangeDone = MutableLiveData<Boolean>()
    val isChangeDone : LiveData<Boolean> get() = _isChangeDone

    private val _errorMessage = MutableLiveData<Int>()
    val errorMessage : LiveData<Int> get() = _errorMessage

    fun getType(typeId: String) {
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

    fun createType(type: Type) {
        repository.createType(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isChangeDone.value = true
                Log.d(TAG, "create survey success")
            }, {
                _errorMessage.value = R.string.error_create_type
                Log.d(TAG, "create survey error")
            })
            .connect()
    }

    fun updateType(type: Type) {
        repository.updateType(type)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isChangeDone.value = true
                    Log.d(TAG, "update type ${type.id} success")
                },
                {
                    _isChangeDone.value = false
                    _errorMessage.value = R.string.error_update_type
                    Log.d(TAG, "update type ${type.id} error")
                }
            ).connect()
    }

    fun deleteType(typeId: String) {
        repository.deleteType(typeId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _isChangeDone.value = true
                    Log.d(TAG, "delete type $typeId success")
                },
                {
                    _errorMessage.value = R.string.error_delete_type
                    Log.d(TAG, "delete type error")
                }
            ).connect()
    }

    companion object {
        private const val TAG = "database"
    }
}