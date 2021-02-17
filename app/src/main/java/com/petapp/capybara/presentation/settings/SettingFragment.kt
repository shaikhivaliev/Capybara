package com.petapp.capybara.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.petapp.capybara.R
import com.petapp.capybara.data.model.Settings
import com.petapp.capybara.presentation.auth.AuthActivity
import kotlinx.android.synthetic.main.fragment_profiles.recycler_view
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel {
        parametersOf(findNavController())
    }

    private val adapter: SettingsAdapter = SettingsAdapter(
        itemClick = { viewModel.openSettingsScreen(it.name) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(recycler_view) {
            this.layoutManager = LinearLayoutManager(context)
            adapter = this@SettingFragment.adapter
        }
        adapter.items =
            listOf(
                Settings(R.drawable.ic_feedback, R.string.settings_feedback),
                Settings(R.drawable.ic_about_app, R.string.settings_about_app),
                Settings(R.drawable.ic_rules, R.string.settings_rules)
            )
        exit.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
