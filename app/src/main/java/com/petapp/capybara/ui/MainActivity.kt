package com.petapp.capybara.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.petapp.capybara.R
import com.petapp.capybara.model.RepositoryImpl
import com.petapp.capybara.presenter.Presenter
import com.petapp.capybara.presenter.PresenterImpl
import com.petapp.capybara.view.MainViewImpl

class MainActivity : AppCompatActivity() {

    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*
        val contentView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        setContentView(contentView)
*/

        //дает вам корневой элемент представления, без необходимости знать его фактическое имя / тип / идентификатор
        setContentView(R.layout.activity_main)
        val contentView = findViewById<View>(android.R.id.content)

        val view = MainViewImpl(contentView)
        val repository = RepositoryImpl()
        presenter = PresenterImpl(view, repository)
        view.onFinishInflate(presenter)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}
