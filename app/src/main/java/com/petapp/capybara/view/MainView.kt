package com.petapp.capybara.view

interface MainView {

    fun showData(data: String)
    fun showError(error: String)
    fun inProgress(isShow: Boolean)

}