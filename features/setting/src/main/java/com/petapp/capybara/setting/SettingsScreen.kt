package com.petapp.capybara.setting

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.petapp.capybara.DeleteButton
import com.petapp.capybara.list.IconTitleItem
import com.petapp.capybara.styles.colorPrimaryDark
import com.petapp.capybara.styles.neutralN50
import com.petapp.capybara.styles.textMedium
import com.petapp.capybara.styles.textSmall

@Composable
fun SettingsScreen(
    signOut: () -> Unit,
    sendEmail: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.settings_capybara_title),
            color = colorPrimaryDark,
            style = textMedium(),
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(R.string.settings_app_version, "1.0.0"),
            color = neutralN50,
            style = textSmall()
        )
        LazyColumn(
            content = {
                items(SettingItems.values()) { item ->
                    IconTitleItem(
                        icon = item.icon,
                        title = item.title
                    ) {
                        when (item) {
                            SettingItems.FEEDBACK -> sendEmail()
                            SettingItems.RATE_APP -> {
                                // navigate to play market
                            }
                            SettingItems.SHARE_LINK -> {
                                // share play market link
                            }
                            SettingItems.RULES -> {
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

enum class SettingItems(
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    FEEDBACK(
        R.string.settings_feedback,
        R.drawable.ic_mail_outline
    ),
    RATE_APP(
        R.string.settings_rate_app,
        R.drawable.ic_start_rate
    ),
    SHARE_LINK(
        R.string.settings_share_link,
        R.drawable.ic_share
    ),
    RULES(
        R.string.settings_rules,
        R.drawable.ic_security
    )
}
