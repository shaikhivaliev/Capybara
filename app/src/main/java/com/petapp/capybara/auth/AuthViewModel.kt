package com.petapp.capybara.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    enum class AuthState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    private val _authenticationState = MutableLiveData<AuthState>()
    val authenticationState: LiveData<AuthState> = _authenticationState

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        if (firebaseAuth.currentUser != null) {
            _authenticationState.value = AuthState.AUTHENTICATED
        } else {
            _authenticationState.value = AuthState.UNAUTHENTICATED
        }
    }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}
