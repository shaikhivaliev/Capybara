package com.petapp.capybara.types

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petapp.capybara.core.data.repository.TypesRepository
import kotlinx.coroutines.launch

class TypesVm(
    private val typesRepository: TypesRepository,
    private val store: TypesStore
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
}
