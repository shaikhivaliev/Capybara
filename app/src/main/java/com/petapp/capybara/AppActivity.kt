package com.petapp.capybara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AppActivity : AppCompatActivity(R.layout.layout_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Screens.navigateTo(this, Screens.Main, R.id.container)
    }

}
