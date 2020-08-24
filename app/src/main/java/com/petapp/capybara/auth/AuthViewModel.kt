package com.petapp.capybara.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.extensions.navigateWith

class AuthViewModel(
    val navController: NavController
) : ViewModel() {

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

    fun openMainScreen() {
        AuthFragmentDirections.toMain().navigateWith(navController)
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}