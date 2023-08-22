package com.petapp.capybara.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.petapp.capybara.MainActivity
import com.petapp.capybara.R

class AuthActivity : AppCompatActivity() {

    private val authHelper: AuthHelper = AuthHelper()

    private val providers: List<AuthUI.IdpConfig> = listOf(
        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private val customAuthUILayout = AuthMethodPickerLayout
        .Builder(R.layout.view_auth_layout)
        .setPhoneButtonId(R.id.auth_phone)
        .setEmailButtonId(R.id.auth_mail)
        .build()

    private val startAuthUI = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when {
            result.resultCode == Activity.RESULT_OK -> openMainScreen()
            result.data == null -> finish()
            else -> Log.d(TAG, "some error")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        with(authHelper) {
            authenticationState.observe(this@AuthActivity) { state ->
                when (state) {
                    AuthHelper.AuthState.AUTHENTICATED -> openMainScreen()
                    AuthHelper.AuthState.UNAUTHENTICATED -> signIn()
                    else -> Log.d(TAG, "some error")
                }
            }
        }
    }

    private fun signIn() {
        val authIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setAuthMethodPickerLayout(customAuthUILayout)
            .setTheme(R.style.AppThemeFirebaseAuth)
            .build()
        startAuthUI.launch(authIntent)
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        private const val TAG = "navigation"
    }
}
