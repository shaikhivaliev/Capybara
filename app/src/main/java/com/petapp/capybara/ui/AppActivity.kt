package com.petapp.capybara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.petapp.capybara.R
import com.petapp.capybara.Screens
import com.petapp.capybara.ui.main.MainFragment
import kotlinx.android.synthetic.main.layout_container.*

class AppActivity : AppCompatActivity(R.layout.layout_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Screens.navigateTo(this, Screens.Main, R.id.container)
    }

}
