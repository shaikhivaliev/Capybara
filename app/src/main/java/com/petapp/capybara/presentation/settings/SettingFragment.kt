package com.petapp.capybara.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.BuildConfig
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Settings
import com.petapp.capybara.presentation.auth.AuthActivity
import com.petapp.capybara.ui.*
import com.petapp.capybara.ui.list.IconTitleItem
import com.petapp.capybara.ui.styles.colorPrimaryDark
import com.petapp.capybara.ui.styles.neutralN50
import com.petapp.capybara.ui.styles.textMedium
import com.petapp.capybara.ui.styles.textSmall

@Suppress("ForbiddenComment")
class SettingFragment : Fragment() {

    private val settingItems = listOf(
        Settings(ID_FEEDBACK, R.drawable.ic_mail_outline, R.string.settings_feedback),
        Settings(ID_RATE_APP, R.drawable.ic_start_rate, R.string.settings_rate_app),
        Settings(ID_SHARE_LINK, R.drawable.ic_share, R.string.settings_share_link),
        Settings(ID_RULES, R.drawable.ic_security, R.string.settings_rules)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(inflater.context).apply {
            setContent {
                MdcTheme {
                    SettingsScreen()
                }
            }
        }
    }

    @Preview
    @Composable
    private fun SettingsScreen() {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.settings_capybara_title),
                color = colorPrimaryDark,
                style = textMedium(),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.settings_app_version, BuildConfig.VERSION_NAME),
                color = neutralN50,
                style = textSmall()
            )
            LazyColumn(
                content = {
                    items(settingItems) { item ->
                        IconTitleItem(
                            icon = item.image,
                            title = item.name
                        ) {
                            when (item.id) {
                                ID_FEEDBACK -> sendEmail()
                                ID_RATE_APP -> {
                                    // navigate to play market
                                }
                                ID_SHARE_LINK -> {
                                    // share play market link
                                }
                                ID_RULES -> {
                                    // navigate to web view with rules
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            )
            DeleteButton(
                title = R.string.settings_exit,
                onClick = { signOut() }
            )
        }
    }

    private fun sendEmail() {
        val address = arrayListOf(
            getString(R.string.dev_email)
        ).toTypedArray()
        val subject = getString(R.string.email_subject)
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, address)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        const val ID_FEEDBACK = 0L
        const val ID_RATE_APP = 1L
        const val ID_SHARE_LINK = 2L
        const val ID_RULES = 3L
    }
}
