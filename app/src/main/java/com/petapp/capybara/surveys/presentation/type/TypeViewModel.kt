package com.petapp.capybara.surveys.presentation.type

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.surveys.domain.TypesRepository
import com.petapp.capybara.surveys.domain.dto.Type
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TypeViewModel(
    private val repository: TypesRepository
) : BaseViewModel() {

    val type = MutableLiveData<Type>()
    val isChangeDone = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun getType(typeId: String) {
        repository.getType(typeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                type.value = it
                Log.d("database", "get type ${it.id} success")
            },
                {
                    errorMessage.value = "error"
                    Log.d("database", "get type error")
                }
            ).connect()
    }

    fun createType(type: Type) {
        repository.createType(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isChangeDone.value = true
                Log.d("database", "create survey success")
            }, {
                errorMessage.value = "error"
                Log.d("database", "create survey error")
            })
            .connect()
    }

    fun updateType(type: Type) {
        repository.updateType(type)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "update type ${type.id} success")
                },
                {
                    isChangeDone.value = false
                    errorMessage.value = "error"
                    Log.d("database", "update type ${type.id} error")
                }
            ).connect()
    }

    fun deleteType(typeId: String) {
        repository.deleteType(typeId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isChangeDone.value = true
                    Log.d("database", "delete type $typeId success")
                },
                {
                    errorMessage.value = "error"
                    Log.d("database", "delete type error")
                }
            ).connect()
    }

}