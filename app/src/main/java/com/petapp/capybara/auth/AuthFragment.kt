package com.petapp.capybara.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.petapp.capybara.R
import com.petapp.capybara.extensions.visible
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModel {
        parametersOf(findNavController())
    }

    private var providers: List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        with(viewModel) {
            authenticationState.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    AuthViewModel.AuthState.AUTHENTICATED -> viewModel.openMainScreen()
                    AuthViewModel.AuthState.UNAUTHENTICATED -> signIn()
                    else -> Log.d(TAG, "Navigation from Main activity is error")
                }
            })
        }
    }

    private fun signIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )

        auth_again.setOnClickListener {
            activity?.apply {
                val intent = intent
                overridePendingTransition(0, 0)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.openMainScreen()
            } else {
                auth_error.visible(true)
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
        private const val TAG = "navigation"
    }
}