package com.petapp.capybara.surveys.presentation.types

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.petapp.capybara.common.BaseViewModel
import com.petapp.capybara.surveys.domain.TypesRepository
import com.petapp.capybara.surveys.domain.dto.Type
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TypesViewModel(
    private val repository: TypesRepository
) : BaseViewModel() {

    val types = MutableLiveData<List<Type>>()
    var errorMessage = MutableLiveData<String>()

    fun getTypes() {
        repository.getTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    types.value = it
                    Log.d("database", "get types success")
                },
                {
                    errorMessage.value = "error"
                    Log.d("database", "get types error")
                }
            ).connect()
    }

}