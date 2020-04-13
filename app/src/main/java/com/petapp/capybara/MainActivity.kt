package com.petapp.capybara

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    companion object {
        const val id = 744
    }

    lateinit var viewModel: DatasetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DatasetViewModel::class.java)
        viewModel.getData(id)
        initObservers()
    }

    private fun initObservers() {
        viewModel.inProgress.observe(this, Observer {
            if (it) toast("Progress...") else toast("Done")
        })
        viewModel.data.observe(this, Observer {
            showSnackMessage(it.departmentCaption)
        })
        viewModel.error.observe(this, Observer {
            showSnackMessage(it)
        })
    }
}
