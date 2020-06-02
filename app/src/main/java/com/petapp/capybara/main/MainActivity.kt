package com.petapp.capybara.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.petapp.capybara.R

class MainActivity : AppCompatActivity(R.layout.activity_container) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainFragment())
            .commit()
    }

}
