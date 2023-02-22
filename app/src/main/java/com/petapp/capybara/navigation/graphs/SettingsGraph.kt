package com.petapp.capybara.navigation.graphs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.R
import com.petapp.capybara.auth.AuthActivity
import com.petapp.capybara.setting.SettingsScreen
import com.petapp.capybara.setting.navigation.SettingNavigationScreen


fun NavGraphBuilder.settingsGraph(
    navController: NavHostController,
    context: Context
) {
    composable(SettingNavigationScreen.route) {
        SettingsScreen(
            signOut = {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            },
            sendEmail = {
                val address = arrayListOf(context.getString(R.string.dev_email)).toTypedArray()
                val subject = context.getString(R.string.email_subject)
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, address)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        )
    }
}
