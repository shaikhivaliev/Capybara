package com.petapp.main.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.petapp.core_api.AppWithFacade
import com.petapp.main.R
import com.petapp.main.di.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_container) {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.create((this.application as AppWithFacade).getFacade()).inject(this)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

}
