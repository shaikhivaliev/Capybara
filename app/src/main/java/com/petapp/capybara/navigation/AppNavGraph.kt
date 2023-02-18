package com.petapp.capybara.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.R
import com.petapp.capybara.auth.AuthActivity
import com.petapp.capybara.calendar.CalendarScreen
import com.petapp.capybara.calendar.navigation.CalendarNavigationScreen
import com.petapp.capybara.healthdiary.HealthDiaryScreen
import com.petapp.capybara.healthdiary.navigation.HealthDiaryNavigationScreen
import com.petapp.capybara.profile.ProfileScreen
import com.petapp.capybara.profile.ProfilesScreen
import com.petapp.capybara.profile.navigation.ProfileNavigationScreen
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen
import com.petapp.capybara.setting.SettingsScreen
import com.petapp.capybara.setting.navigation.SettingNavigationScreen
import com.petapp.capybara.survey.SurveyScreen
import com.petapp.capybara.survey.SurveysScreen
import com.petapp.capybara.survey.navigation.SurveyNavigationScreen
import com.petapp.capybara.survey.navigation.SurveysNavigationScreen
import com.petapp.capybara.types.TypesScreen
import com.petapp.capybara.types.navigation.TypesNavigationScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = ProfilesNavigationScreen.route
    ) {
        composable(CalendarNavigationScreen.route) {
            CalendarScreen(
                openNewSurveyScreen = {},
                openProfileScreen = {}
            )
        }
        composable(HealthDiaryNavigationScreen.route) {
            HealthDiaryScreen()
        }
        composable(ProfileNavigationScreen.route) {
            ProfileScreen(
                openProfilesScreen = {}
            )
        }
        composable(ProfilesNavigationScreen.route) {
            ProfilesScreen(
                openNewProfile = {},
                openProfileScreen = {}
            )
        }
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

        composable(SurveyNavigationScreen.route) {
            SurveyScreen()
        }

        composable(SurveysNavigationScreen.route) {
            SurveysScreen(
                openNewSurveyScreen = {},
                openSurveyScreen = {}
            )
        }

        composable(TypesNavigationScreen.route) {
            TypesScreen(
                openHealthDiary = {},
                openSurveysScreen = {}
            )
        }
    }
}
