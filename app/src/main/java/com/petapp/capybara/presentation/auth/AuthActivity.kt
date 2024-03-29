package com.petapp.capybara.presentation.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.petapp.capybara.R
import com.petapp.capybara.databinding.ActivityAuthBinding
import com.petapp.capybara.presentation.main.MainActivity

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    private val authHelper: AuthHelper = AuthHelper()

    private val viewBinding by viewBinding(ActivityAuthBinding::bind)

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
            else -> viewBinding.error.isVisible = true
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
                    else -> Log.d(TAG, "Navigation from Main activity is error")
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

        viewBinding.tryAgain.setOnClickListener {
            val intent = intent
            overridePendingTransition(0, 0)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
        }
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
