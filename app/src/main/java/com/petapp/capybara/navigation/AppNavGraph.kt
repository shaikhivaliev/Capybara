package com.petapp.capybara.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.R
import com.petapp.capybara.auth.AuthActivity
import com.petapp.capybara.calendar.presentation.CalendarScreen
import com.petapp.capybara.calendar.navigation.CalendarNavigationScreen
import com.petapp.capybara.healthdiary.presentation.HealthDiaryScreen
import com.petapp.capybara.healthdiary.navigation.HealthDiaryNavigationScreen
import com.petapp.capybara.profile.presentation.ProfileScreen
import com.petapp.capybara.profile.presentation.ProfilesScreen
import com.petapp.capybara.profile.navigation.ProfileNavigation
import com.petapp.capybara.profile.navigation.ProfilesNavigationScreen
import com.petapp.capybara.setting.SettingsScreen
import com.petapp.capybara.setting.navigation.SettingNavigationScreen
import com.petapp.capybara.survey.presentation.SurveyScreen
import com.petapp.capybara.survey.presentation.SurveysScreen
import com.petapp.capybara.survey.navigation.SurveyNavigationScreen
import com.petapp.capybara.survey.navigation.SurveysNavigationScreen
import com.petapp.capybara.types.TypesScreen
import com.petapp.capybara.types.navigation.TypesNavigationScreen

// todo separate to several graphs
@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = ProfilesNavigationScreen.route
    ) {
        composable(CalendarNavigationScreen.route) {
            CalendarScreen(
                openNewSurveyScreen = {
                    navController.navigate(SurveyNavigationScreen.route)
                },
                openProfilesScreen = {
                    navController.navigate(ProfilesNavigationScreen.route)
                }
            )
        }
        composable(HealthDiaryNavigationScreen.route) {
            HealthDiaryScreen(
                openProfilesScreen = {
                    navController.navigate(ProfilesNavigationScreen.route)
                }
            )
        }
        composable(
            route = ProfileNavigation.routeWithArgs,
            arguments = ProfileNavigation.arguments
        ) { navBackStackEntry ->
            val profileId = navBackStackEntry.arguments?.getLong(ProfileNavigation.typeArg)
            ProfileScreen(
                profileId = profileId,
                openProfilesScreen = {
                    navController.navigate(ProfilesNavigationScreen.route)
                }
            )
        }
        composable(route = ProfileNavigation.route) {
            ProfileScreen(
                openProfilesScreen = {
                    navController.navigate(ProfilesNavigationScreen.route)
                }
            )
        }
        composable(route = ProfilesNavigationScreen.route) {
            ProfilesScreen(
                openNewProfile = {
                    navController.navigate(ProfileNavigation.route)
                },
                openProfileScreen = {
                    navController.navigate("${ProfileNavigation.route}/${it}")
                }
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
                openProfilesScreen = {
                    navController.navigate(ProfilesNavigationScreen.route)
                },
                openNewSurveyScreen = {
                    navController.navigate(SurveyNavigationScreen.route)
                },
                openSurveyScreen = {
                    navController.navigate(SurveyNavigationScreen.route)
                }
            )
        }

        composable(TypesNavigationScreen.route) {
            TypesScreen(
                openHealthDiary = {
                    navController.navigate(HealthDiaryNavigationScreen.route)
                },
                openSurveysScreen = {
                    navController.navigate(SurveysNavigationScreen.route)
                }
            )
        }
    }
}
