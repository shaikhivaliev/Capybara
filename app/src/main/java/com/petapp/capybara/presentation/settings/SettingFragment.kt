package com.petapp.capybara.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.BuildConfig
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Settings
import com.petapp.capybara.presentation.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_profiles.recycler_view
import kotlinx.android.synthetic.main.fragment_settings.*

@Suppress("ForbiddenComment")
class SettingFragment : Fragment(R.layout.fragment_settings) {

    private val adapter: SettingsAdapter = SettingsAdapter(
        itemClick = {
            when (it.id) {
                ID_FEEDBACK -> sendEmail()
                ID_RATE_APP -> {
                    // todo navigate to play market
                }
                ID_SHARE_LINK -> {
                    // todo share play market link
                }
                ID_RULES -> {
                    // todo navigate to web view with rules
                }
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initViews()
    }

    private fun initViews() {
        app_version.text = getString(R.string.settings_app_version, BuildConfig.VERSION_NAME)
        exit.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun initRecycler() {
        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@SettingFragment.adapter
        }
        adapter.items =
            listOf(
                Settings(ID_FEEDBACK, R.drawable.ic_mail_outline, R.string.settings_feedback),
                Settings(ID_RATE_APP, R.drawable.ic_start_rate, R.string.settings_rate_app),
                Settings(ID_SHARE_LINK, R.drawable.ic_share, R.string.settings_share_link),
                Settings(ID_RULES, R.drawable.ic_security, R.string.settings_rules)
            )
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

    companion object {
        const val ID_FEEDBACK = 0L
        const val ID_RATE_APP = 1L
        const val ID_SHARE_LINK = 2L
        const val ID_RULES = 3L
    }
}
